package com.deenora.app.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.deenora.app.data.prayer.AsrJuristicMethod
import com.deenora.app.data.prayer.CalculationMethod
import com.deenora.app.data.prayer.CityLocation
import com.deenora.app.data.prayer.PredefinedCities
import com.deenora.app.ui.i18n.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UserSettings(
    val language: AppLanguage = AppLanguage.ARABIC,
    val city: CityLocation = PredefinedCities.DEFAULT_CITY,
    val calculationMethod: CalculationMethod = CalculationMethod.UMM_AL_QURA,
    val asrMethod: AsrJuristicMethod = AsrJuristicMethod.STANDARD,
    val hijriAdjustment: Int = 0,
    val isDarkMode: Boolean? = null, // null = system default
    val isVibrationEnabled: Boolean = true,
    val isNotificationEnabled: Boolean = true,
    val quranFontSize: Float = 22f,
    val dailyDhikrGoal: Int = 300,
    val isDhikrSoundEnabled: Boolean = false
)

class UserPreferencesRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("deenora_user_prefs", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<UserSettings> = _settings.asStateFlow()

    private fun loadSettings(): UserSettings {
        val langCode = prefs.getString("key_lang", AppLanguage.ARABIC.code) ?: AppLanguage.ARABIC.code
        val lang = AppLanguage.values().firstOrNull { it.code == langCode } ?: AppLanguage.ARABIC

        val cityName = prefs.getString("key_city_name", PredefinedCities.DEFAULT_CITY.nameEn)
        val city = PredefinedCities.CITIES.firstOrNull { it.nameEn == cityName } ?: PredefinedCities.DEFAULT_CITY

        val methodId = prefs.getString("key_method", CalculationMethod.UMM_AL_QURA.id)
        val method = CalculationMethod.values().firstOrNull { it.id == methodId } ?: CalculationMethod.UMM_AL_QURA

        val asrFactor = prefs.getInt("key_asr", AsrJuristicMethod.STANDARD.factor)
        val asr = if (asrFactor == 2) AsrJuristicMethod.HANAFI else AsrJuristicMethod.STANDARD

        val hijriAdj = prefs.getInt("key_hijri_adj", 0)
        val darkModeSaved = if (prefs.contains("key_dark_mode")) prefs.getBoolean("key_dark_mode", false) else null
        val vibration = prefs.getBoolean("key_vibration", true)
        val notification = prefs.getBoolean("key_notification", true)
        val fontSize = prefs.getFloat("key_quran_font_size", 22f)
        val goal = prefs.getInt("key_daily_dhikr_goal", 300)
        val sound = prefs.getBoolean("key_dhikr_sound", false)

        return UserSettings(
            language = lang,
            city = city,
            calculationMethod = method,
            asrMethod = asr,
            hijriAdjustment = hijriAdj,
            isDarkMode = darkModeSaved,
            isVibrationEnabled = vibration,
            isNotificationEnabled = notification,
            quranFontSize = fontSize,
            dailyDhikrGoal = goal,
            isDhikrSoundEnabled = sound
        )
    }

    fun updateLanguage(language: AppLanguage) {
        prefs.edit().putString("key_lang", language.code).apply()
        _settings.value = _settings.value.copy(language = language)
    }

    fun updateCity(city: CityLocation) {
        prefs.edit().putString("key_city_name", city.nameEn).apply()
        _settings.value = _settings.value.copy(city = city)
    }

    fun updateCalculationMethod(method: CalculationMethod) {
        prefs.edit().putString("key_method", method.id).apply()
        _settings.value = _settings.value.copy(calculationMethod = method)
    }

    fun updateAsrMethod(asr: AsrJuristicMethod) {
        prefs.edit().putInt("key_asr", asr.factor).apply()
        _settings.value = _settings.value.copy(asrMethod = asr)
    }

    fun updateHijriAdjustment(days: Int) {
        prefs.edit().putInt("key_hijri_adj", days).apply()
        _settings.value = _settings.value.copy(hijriAdjustment = days)
    }

    fun updateDarkMode(darkMode: Boolean?) {
        if (darkMode == null) {
            prefs.edit().remove("key_dark_mode").apply()
        } else {
            prefs.edit().putBoolean("key_dark_mode", darkMode).apply()
        }
        _settings.value = _settings.value.copy(isDarkMode = darkMode)
    }

    fun updateVibration(enabled: Boolean) {
        prefs.edit().putBoolean("key_vibration", enabled).apply()
        _settings.value = _settings.value.copy(isVibrationEnabled = enabled)
    }

    fun updateNotification(enabled: Boolean) {
        prefs.edit().putBoolean("key_notification", enabled).apply()
        _settings.value = _settings.value.copy(isNotificationEnabled = enabled)
    }

    fun updateQuranFontSize(size: Float) {
        prefs.edit().putFloat("key_quran_font_size", size).apply()
        _settings.value = _settings.value.copy(quranFontSize = size)
    }

    fun updateDailyDhikrGoal(goal: Int) {
        prefs.edit().putInt("key_daily_dhikr_goal", goal).apply()
        _settings.value = _settings.value.copy(dailyDhikrGoal = goal)
    }

    fun updateDhikrSound(enabled: Boolean) {
        prefs.edit().putBoolean("key_dhikr_sound", enabled).apply()
        _settings.value = _settings.value.copy(isDhikrSoundEnabled = enabled)
    }
}
