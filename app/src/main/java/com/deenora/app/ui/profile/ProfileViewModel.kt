package com.deenora.app.ui.profile

import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deenora.app.data.prayer.AsrJuristicMethod
import com.deenora.app.data.prayer.CalculationMethod
import com.deenora.app.data.prayer.CityLocation
import com.deenora.app.data.preferences.UserPreferencesRepository
import com.deenora.app.data.preferences.UserSettings
import com.deenora.app.service.PrayerWorkManagerScheduler
import com.deenora.app.ui.i18n.AppLanguage
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.TimeZone

data class ProfileUiState(
    val settings: UserSettings = UserSettings(),
    val isDetectingGps: Boolean = false,
    val gpsError: String? = null
)

class ProfileViewModel(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.settings.collect { settings ->
                _uiState.update { it.copy(settings = settings) }
            }
        }
    }

    fun setLanguage(language: AppLanguage) {
        preferencesRepository.updateLanguage(language)
    }

    fun setCity(city: CityLocation, context: Context? = null) {
        preferencesRepository.updateCity(city)
        context?.let { PrayerWorkManagerScheduler.scheduleAllPrayerNotifications(it) }
    }

    fun setCalculationMethod(method: CalculationMethod, context: Context? = null) {
        preferencesRepository.updateCalculationMethod(method)
        context?.let { PrayerWorkManagerScheduler.scheduleAllPrayerNotifications(it) }
    }

    fun setAsrMethod(asr: AsrJuristicMethod, context: Context? = null) {
        preferencesRepository.updateAsrMethod(asr)
        context?.let { PrayerWorkManagerScheduler.scheduleAllPrayerNotifications(it) }
    }

    fun setHijriAdjustment(days: Int) {
        preferencesRepository.updateHijriAdjustment(days)
    }

    fun setDarkMode(dark: Boolean?) {
        preferencesRepository.updateDarkMode(dark)
    }

    fun setVibration(enabled: Boolean) {
        preferencesRepository.updateVibration(enabled)
    }

    fun setNotification(enabled: Boolean, context: Context) {
        preferencesRepository.updateNotification(enabled)
        if (enabled) {
            PrayerWorkManagerScheduler.scheduleAllPrayerNotifications(context)
        } else {
            PrayerWorkManagerScheduler.cancelAllPrayerNotifications(context)
        }
    }

    fun detectGpsLocation(context: Context) {
        _uiState.update { it.copy(isDetectingGps = true, gpsError = null) }
        try {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val providers = lm.getProviders(true)
            var bestLocation: Location? = null
            for (p in providers) {
                val l = lm.getLastKnownLocation(p) ?: continue
                if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                    bestLocation = l
                }
            }

            if (bestLocation != null) {
                val tzHours = TimeZone.getDefault().rawOffset / 3600000.0
                val detected = CityLocation(
                    nameEn = "Current Location",
                    nameAr = "الموقع الحالي",
                    nameFr = "Position Actuelle",
                    countryEn = "GPS",
                    countryAr = "تحديد آلي",
                    latitude = bestLocation.latitude,
                    longitude = bestLocation.longitude,
                    timezoneOffsetHours = tzHours,
                    isGps = true
                )
                setCity(detected, context)
            } else {
                _uiState.update { it.copy(gpsError = "Could not retrieve GPS coordinates. Using selected city.") }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(gpsError = e.localizedMessage) }
        } finally {
            _uiState.update { it.copy(isDetectingGps = false) }
        }
    }
}
