package com.deenora.app.ui.discover

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deenora.app.data.local.dao.TasbihDao
import com.deenora.app.data.local.entity.CustomDhikrEntity
import com.deenora.app.data.local.entity.DhikrDailyLogEntity
import com.deenora.app.data.local.entity.TasbihRecordEntity
import com.deenora.app.data.location.LocationHelper
import com.deenora.app.data.names.NameOfAllah
import com.deenora.app.data.names.NamesOfAllahRepository
import com.deenora.app.data.prayer.CityLocation
import com.deenora.app.data.preferences.UserPreferencesRepository
import com.deenora.app.data.preferences.UserSettings
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.*

enum class DiscoverSection {
    HUB,
    QIBLA,
    TASBIH,
    NAMES_OF_ALLAH,
    CALENDAR
}

data class DhikrPreset(
    val key: String,
    val arabic: String,
    val transliteration: String,
    val translationEn: String,
    val translationFr: String,
    val isCustom: Boolean = false,
    val customId: Long? = null
)

data class DiscoverUiState(
    val currentSection: DiscoverSection = DiscoverSection.HUB,
    val settings: UserSettings = UserSettings(),
    // Qibla state
    val compassAzimuth: Float = 0f,
    val qiblaBearing: Float = 0f,
    val distanceToKaabaKm: Double = 0.0,
    val isQiblaAligned: Boolean = false,
    val relativeAngle: Float = 0f,
    val turnDirectionEn: String = "",
    val turnDirectionAr: String = "",
    val turnDirectionFr: String = "",
    val hasCompassSensor: Boolean = true,
    val isGpsDetecting: Boolean = false,
    // Tasbih state
    val currentDhikr: DhikrPreset = PRESET_DHIKRS[0],
    val tasbihCount: Int = 0,
    val tasbihTarget: Int = 33,
    val completedRounds: Int = 0,
    val totalLifetimeCount: Long = 0,
    val customDhikrs: List<CustomDhikrEntity> = emptyList(),
    val todayDhikrCount: Int = 0,
    val dailyGoal: Int = 300,
    val isDailyGoalAchieved: Boolean = false,
    val recentDailyLogs: List<DhikrDailyLogEntity> = emptyList(),
    val isSoundEnabled: Boolean = false,
    val isVibrationEnabled: Boolean = true,
    // Names of Allah
    val namesQuery: String = "",
    val filteredNames: List<NameOfAllah> = NamesOfAllahRepository.NAMES
) {
    companion object {
        val PRESET_DHIKRS = listOf(
            DhikrPreset("subhanallah", "سُبْحَانَ اللَّهِ", "SubhanAllah", "Glory be to Allah", "Gloire à Allah"),
            DhikrPreset("alhamdulillah", "الْحَمْدُ لِلَّهِ", "Alhamdulillah", "Praise be to Allah", "Louange à Allah"),
            DhikrPreset("allahuakbar", "اللَّهُ أَكْبَرُ", "Allahu Akbar", "Allah is the Greatest", "Allah est le Plus Grand"),
            DhikrPreset("lailahaillallah", "لَا إِلَٰهَ إِلَّا اللَّهُ", "La ilaha illallah", "None has the right to be worshipped but Allah", "Nulle divinité n'est digne d'être adorée sauf Allah"),
            DhikrPreset("astaghfirullah", "أَسْتَغْفِرُ اللَّهَ", "Astaghfirullah", "I seek forgiveness from Allah", "Je demande pardon à Allah"),
            DhikrPreset("salawat", "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ", "Allahumma salli 'ala Muhammad", "O Allah, send blessings upon Muhammad", "Ô Allah, répands Tes bénédictions sur Muhammad"),
            DhikrPreset("hawqala", "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ", "La hawla wa la quwwata illa billah", "There is no might nor power except with Allah", "Il n'y a de force ni de puissance que par Allah")
        )
    }
}

class DiscoverViewModel(
    private val tasbihDao: TasbihDao,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel(), SensorEventListener {

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    private var sensorManager: SensorManager? = null
    private var rotationVectorSensor: Sensor? = null
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null

    // Low-pass filtered sensor matrices
    private val gravity = FloatArray(3)
    private val geomagnetic = FloatArray(3)
    private var hasGravity = false
    private var hasGeomagnetic = false

    private var wasAlignedLastFrame = false
    private var appContext: Context? = null

    private val todayDateKey: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date())

    init {
        // Collect user settings and calculate Qibla direction to Kaaba
        viewModelScope.launch {
            preferencesRepository.settings.collect { settings ->
                calculateQiblaBearing(settings.city.latitude, settings.city.longitude)
                _uiState.update {
                    it.copy(
                        settings = settings,
                        dailyGoal = settings.dailyDhikrGoal,
                        isSoundEnabled = settings.isDhikrSoundEnabled,
                        isVibrationEnabled = settings.isVibrationEnabled
                    )
                }
            }
        }

        // Collect custom dhikrs from Room
        viewModelScope.launch {
            tasbihDao.getAllCustomDhikrs().collect { customList ->
                _uiState.update { it.copy(customDhikrs = customList) }
            }
        }

        // Collect today's daily log from Room
        viewModelScope.launch {
            tasbihDao.observeDailyLog(todayDateKey).collect { log ->
                val count = log?.totalCount ?: 0
                val goal = _uiState.value.dailyGoal
                _uiState.update {
                    it.copy(
                        todayDhikrCount = count,
                        isDailyGoalAchieved = count >= goal && goal > 0
                    )
                }
            }
        }

        // Collect recent daily logs from Room
        viewModelScope.launch {
            tasbihDao.getRecentDailyLogs().collect { recentLogs ->
                _uiState.update { it.copy(recentDailyLogs = recentLogs) }
            }
        }

        // Collect initial tasbih record
        viewModelScope.launch {
            loadTasbihRecord(_uiState.value.currentDhikr.key)
        }
    }

    fun navigateToSection(section: DiscoverSection) {
        _uiState.update { it.copy(currentSection = section) }
    }

    // --- Qibla Calculation & Sensors ---
    fun initSensors(context: Context) {
        appContext = context.applicationContext
        val sm = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager = sm
        rotationVectorSensor = sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        val hasSensors = rotationVectorSensor != null || (accelerometer != null && magnetometer != null)
        _uiState.update { it.copy(hasCompassSensor = hasSensors) }

        if (rotationVectorSensor != null) {
            sm.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_GAME)
        } else {
            accelerometer?.let { sm.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME) }
            magnetometer?.let { sm.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME) }
        }
    }

    fun releaseSensors() {
        sensorManager?.unregisterListener(this)
        appContext = null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            val rotationMatrix = FloatArray(9)
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            val orientation = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, orientation)
            val azimuthDeg = (Math.toDegrees(orientation[0].toDouble()) + 360.0) % 360.0
            updateCompass(azimuthDeg.toFloat())
        } else {
            val alpha = 0.15f
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                gravity[0] = gravity[0] + alpha * (event.values[0] - gravity[0])
                gravity[1] = gravity[1] + alpha * (event.values[1] - gravity[1])
                gravity[2] = gravity[2] + alpha * (event.values[2] - gravity[2])
                hasGravity = true
            } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                geomagnetic[0] = geomagnetic[0] + alpha * (event.values[0] - geomagnetic[0])
                geomagnetic[1] = geomagnetic[1] + alpha * (event.values[1] - geomagnetic[1])
                geomagnetic[2] = geomagnetic[2] + alpha * (event.values[2] - geomagnetic[2])
                hasGeomagnetic = true
            }

            if (hasGravity && hasGeomagnetic) {
                val r = FloatArray(9)
                val i = FloatArray(9)
                if (SensorManager.getRotationMatrix(r, i, gravity, geomagnetic)) {
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(r, orientation)
                    val azimuthDeg = (Math.toDegrees(orientation[0].toDouble()) + 360.0) % 360.0
                    updateCompass(azimuthDeg.toFloat())
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    fun setManualAzimuth(azimuth: Float) {
        updateCompass(azimuth)
    }

    fun setCity(city: CityLocation) {
        preferencesRepository.updateCity(city)
        calculateQiblaBearing(city.latitude, city.longitude)
    }

    fun refreshLocationWithGps(context: Context) {
        _uiState.update { it.copy(isGpsDetecting = true) }
        LocationHelper.fetchCurrentLocation(
            context = context,
            onSuccess = { loc ->
                _uiState.update { it.copy(isGpsDetecting = false) }
                setCity(loc)
            },
            onError = {
                _uiState.update { it.copy(isGpsDetecting = false) }
            }
        )
    }

    private fun updateCompass(azimuth: Float) {
        val qibla = _uiState.value.qiblaBearing
        val relAngle = (qibla - azimuth + 360f) % 360f
        val diff = abs(azimuth - qibla)
        val isAligned = diff <= 4.0f || diff >= 356.0f

        val turnAngle = if (relAngle > 180f) (360f - relAngle).toInt() else relAngle.toInt()
        val (dirEn, dirAr, dirFr) = when {
            isAligned -> Triple("Facing Kaaba Directly ✓", "أنت باتجاه القبلة الشريفة ✓", "Face à la Kaaba ✓")
            relAngle <= 180f -> Triple("Turn Right ${turnAngle}°", "أدر يميناً ${turnAngle}°", "Tournez à droite ${turnAngle}°")
            else -> Triple("Turn Left ${turnAngle}°", "أدر يساراً ${turnAngle}°", "Tournez à gauche ${turnAngle}°")
        }

        if (isAligned && !wasAlignedLastFrame && _uiState.value.settings.isVibrationEnabled) {
            appContext?.let { performQiblaAlignedHaptic(it) }
        }
        wasAlignedLastFrame = isAligned

        _uiState.update {
            it.copy(
                compassAzimuth = azimuth,
                relativeAngle = relAngle,
                isQiblaAligned = isAligned,
                turnDirectionEn = dirEn,
                turnDirectionAr = dirAr,
                turnDirectionFr = dirFr
            )
        }
    }

    private fun performQiblaAlignedHaptic(context: Context) {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
            if (vibrator?.hasVibrator() == true) {
                vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 40, 30, 80), -1))
            }
        } catch (_: Exception) {}
    }

    private fun calculateQiblaBearing(lat: Double, lng: Double) {
        val kaabaLat = Math.toRadians(21.422487)
        val kaabaLng = Math.toRadians(39.826206)
        val phi1 = Math.toRadians(lat)
        val deltaLambda = kaabaLng - Math.toRadians(lng)

        val y = sin(deltaLambda) * cos(kaabaLat)
        val x = cos(phi1) * sin(kaabaLat) - sin(phi1) * cos(kaabaLat) * cos(deltaLambda)
        var qibla = Math.toDegrees(atan2(y, x))
        qibla = (qibla + 360.0) % 360.0

        val r = 6371.0
        val dLat = kaabaLat - phi1
        val a = sin(dLat / 2.0).pow(2.0) + cos(phi1) * cos(kaabaLat) * sin(deltaLambda / 2.0).pow(2.0)
        val c = 2.0 * atan2(sqrt(a), sqrt(1.0 - a))
        val distance = r * c

        _uiState.update {
            it.copy(
                qiblaBearing = qibla.toFloat(),
                distanceToKaabaKm = distance
            )
        }
    }

    // --- Interactive Tasbih Methods ---
    fun selectDhikr(preset: DhikrPreset) {
        _uiState.update {
            it.copy(
                currentDhikr = preset,
                tasbihCount = 0,
                completedRounds = 0
            )
        }
        viewModelScope.launch {
            loadTasbihRecord(preset.key)
        }
    }

    fun selectCustomDhikr(custom: CustomDhikrEntity) {
        val preset = DhikrPreset(
            key = "custom_${custom.id}",
            arabic = custom.arabic,
            transliteration = custom.transliteration,
            translationEn = custom.translation,
            translationFr = custom.translation,
            isCustom = true,
            customId = custom.id
        )
        _uiState.update {
            it.copy(
                currentDhikr = preset,
                tasbihTarget = custom.target,
                tasbihCount = custom.count,
                completedRounds = 0,
                totalLifetimeCount = custom.lifetimeCount
            )
        }
    }

    fun setTasbihTarget(target: Int) {
        _uiState.update { it.copy(tasbihTarget = target, tasbihCount = 0, completedRounds = 0) }
    }

    fun setCustomCount(count: Int) {
        val safeCount = count.coerceAtLeast(0)
        _uiState.update { it.copy(tasbihCount = safeCount) }
        viewModelScope.launch {
            val currentKey = _uiState.value.currentDhikr.key
            tasbihDao.setCount(currentKey, safeCount)
        }
    }

    fun setDailyGoal(goal: Int) {
        val safeGoal = goal.coerceAtLeast(1)
        preferencesRepository.updateDailyDhikrGoal(safeGoal)
        _uiState.update {
            it.copy(
                dailyGoal = safeGoal,
                isDailyGoalAchieved = it.todayDhikrCount >= safeGoal
            )
        }
        viewModelScope.launch {
            val currentLog = tasbihDao.getDailyLog(todayDateKey)
            tasbihDao.insertOrUpdateDailyLog(
                DhikrDailyLogEntity(
                    dateKey = todayDateKey,
                    totalCount = currentLog?.totalCount ?: 0,
                    dailyGoal = safeGoal,
                    completedSets = currentLog?.completedSets ?: 0
                )
            )
        }
    }

    fun toggleSound() {
        val next = !_uiState.value.isSoundEnabled
        preferencesRepository.updateDhikrSound(next)
        _uiState.update { it.copy(isSoundEnabled = next) }
    }

    fun toggleVibration() {
        val next = !_uiState.value.isVibrationEnabled
        preferencesRepository.updateVibration(next)
        _uiState.update { it.copy(isVibrationEnabled = next) }
    }

    fun addCustomDhikr(arabic: String, transliteration: String, translation: String, target: Int) {
        viewModelScope.launch {
            val entity = CustomDhikrEntity(
                arabic = arabic.ifBlank { "ذكر مخصص" },
                transliteration = transliteration.ifBlank { "Dhikr" },
                translation = translation.ifBlank { "Custom Dhikr" },
                target = target.coerceAtLeast(1),
                count = 0,
                lifetimeCount = 0
            )
            val id = tasbihDao.insertCustomDhikr(entity)
            selectCustomDhikr(entity.copy(id = id))
        }
    }

    fun deleteCustomDhikr(custom: CustomDhikrEntity) {
        viewModelScope.launch {
            tasbihDao.deleteCustomDhikr(custom)
            if (_uiState.value.currentDhikr.customId == custom.id) {
                selectDhikr(DiscoverUiState.PRESET_DHIKRS[0])
            }
        }
    }

    fun tapTasbih(context: Context) {
        val current = _uiState.value.tasbihCount
        val target = _uiState.value.tasbihTarget
        val isTargetReached = target > 0 && (current + 1) >= target

        val nextCount = if (isTargetReached) 0 else current + 1
        val nextRounds = if (isTargetReached) _uiState.value.completedRounds + 1 else _uiState.value.completedRounds
        val nextLifetime = _uiState.value.totalLifetimeCount + 1
        val nextTodayCount = _uiState.value.todayDhikrCount + 1
        val isDailyGoalJustAchieved = nextTodayCount == _uiState.value.dailyGoal

        _uiState.update {
            it.copy(
                tasbihCount = nextCount,
                completedRounds = nextRounds,
                totalLifetimeCount = nextLifetime,
                todayDhikrCount = nextTodayCount,
                isDailyGoalAchieved = nextTodayCount >= it.dailyGoal && it.dailyGoal > 0
            )
        }

        // Haptic feedback
        if (_uiState.value.isVibrationEnabled) {
            performTasbihHaptic(context, isComplete = isTargetReached || isDailyGoalJustAchieved)
        }

        // Sound feedback
        if (_uiState.value.isSoundEnabled) {
            try {
                val am = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
                am?.playSoundEffect(AudioManager.FX_KEY_CLICK, 0.8f)
            } catch (_: Exception) {}
        }

        // Persist to Room
        viewModelScope.launch {
            val currentDhikr = _uiState.value.currentDhikr
            if (currentDhikr.isCustom && currentDhikr.customId != null) {
                tasbihDao.updateCustomDhikrCount(currentDhikr.customId, nextCount)
            } else {
                tasbihDao.insertOrUpdate(
                    TasbihRecordEntity(
                        dhikrKey = currentDhikr.key,
                        dhikrArabic = currentDhikr.arabic,
                        dhikrTransliteration = currentDhikr.transliteration,
                        count = nextCount,
                        target = target,
                        totalLifetimeCount = nextLifetime
                    )
                )
            }

            // Update Daily Log in Room
            val currentLog = tasbihDao.getDailyLog(todayDateKey)
            val updatedSets = (currentLog?.completedSets ?: 0) + (if (isTargetReached) 1 else 0)
            tasbihDao.insertOrUpdateDailyLog(
                DhikrDailyLogEntity(
                    dateKey = todayDateKey,
                    totalCount = nextTodayCount,
                    dailyGoal = _uiState.value.dailyGoal,
                    completedSets = updatedSets
                )
            )
        }
    }

    fun resetTasbih() {
        _uiState.update { it.copy(tasbihCount = 0, completedRounds = 0) }
        viewModelScope.launch {
            val currentDhikr = _uiState.value.currentDhikr
            if (currentDhikr.isCustom && currentDhikr.customId != null) {
                tasbihDao.updateCustomDhikrCount(currentDhikr.customId, 0)
            } else {
                tasbihDao.resetDhikr(currentDhikr.key)
            }
        }
    }

    private suspend fun loadTasbihRecord(key: String) {
        val record = tasbihDao.getRecordByKey(key)
        if (record != null) {
            _uiState.update {
                it.copy(
                    tasbihCount = record.count,
                    tasbihTarget = record.target,
                    totalLifetimeCount = record.totalLifetimeCount
                )
            }
        }
    }

    private fun performTasbihHaptic(context: Context, isComplete: Boolean) {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
            if (vibrator?.hasVibrator() == true) {
                if (isComplete) {
                    vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 50, 40, 50, 40, 90), -1))
                } else {
                    vibrator.vibrate(VibrationEffect.createOneShot(25, VibrationEffect.DEFAULT_AMPLITUDE))
                }
            }
        } catch (_: Exception) {}
    }

    // --- Names of Allah ---
    fun onNamesQueryChanged(query: String) {
        val trimmed = query.trim()
        val filtered = if (trimmed.isEmpty()) {
            NamesOfAllahRepository.NAMES
        } else {
            NamesOfAllahRepository.NAMES.filter {
                it.arabic.contains(trimmed) ||
                it.transliteration.contains(trimmed, ignoreCase = true) ||
                it.meaningEn.contains(trimmed, ignoreCase = true) ||
                it.meaningFr.contains(trimmed, ignoreCase = true) ||
                it.number.toString() == trimmed
            }
        }
        _uiState.update { it.copy(namesQuery = query, filteredNames = filtered) }
    }
}
