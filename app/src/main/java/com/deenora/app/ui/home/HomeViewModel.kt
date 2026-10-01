package com.deenora.app.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deenora.app.data.azkar.AzkarRepository
import com.deenora.app.data.azkar.DailyDua
import com.deenora.app.data.azkar.IslamicReminder
import com.deenora.app.data.location.LocationHelper
import com.deenora.app.data.prayer.*
import com.deenora.app.data.preferences.UserPreferencesRepository
import com.deenora.app.data.preferences.UserSettings
import com.deenora.app.data.quran.Ayah
import com.deenora.app.data.quran.QuranRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Calendar

data class HomeUiState(
    val currentTimeMillis: Long = System.currentTimeMillis(),
    val settings: UserSettings = UserSettings(),
    val prayerTimes: PrayerTimes? = null,
    val nextPrayerInfo: NextPrayerInfo? = null,
    val currentPrayer: PrayerType = PrayerType.FAJR,
    val hijriDate: HijriDate = HijriCalendarHelper.getHijriDate(),
    val dailyVerse: Ayah = QuranRepository.getDailyVerse(),
    val dailyDua: DailyDua = AzkarRepository.getDailyDua(),
    val dailyReminder: IslamicReminder = AzkarRepository.getDailyReminder(),
    val isCopiedNotificationVisible: Boolean = false,
    val isLocationDetecting: Boolean = false,
    val locationErrorMessage: String? = null
)

class HomeViewModel(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // Collect user settings and trigger real prayer calculation
        viewModelScope.launch {
            preferencesRepository.settings.collect { settings ->
                recalculatePrayers(settings)
            }
        }

        // Live 1-second countdown ticker loop
        viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                val now = System.currentTimeMillis()
                val currentTimes = _uiState.value.prayerTimes
                if (currentTimes != null) {
                    val next = currentTimes.getNextPrayer(now)
                    val active = currentTimes.getCurrentPrayer(now)
                    _uiState.update { it.copy(currentTimeMillis = now, nextPrayerInfo = next, currentPrayer = active) }
                }
            }
        }
    }

    fun setCity(city: CityLocation) {
        preferencesRepository.updateCity(city)
    }

    fun detectLocation(context: Context) {
        _uiState.update { it.copy(isLocationDetecting = true, locationErrorMessage = null) }
        LocationHelper.fetchCurrentLocation(
            context = context,
            onSuccess = { loc ->
                _uiState.update { it.copy(isLocationDetecting = false) }
                setCity(loc)
            },
            onError = { err ->
                _uiState.update { it.copy(isLocationDetecting = false, locationErrorMessage = err) }
            }
        )
    }

    fun dismissLocationError() {
        _uiState.update { it.copy(locationErrorMessage = null) }
    }

    private fun recalculatePrayers(settings: UserSettings) {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val times = PrayerTimeCalculator.calculate(
            year = year,
            month = month,
            day = day,
            latitude = settings.city.latitude,
            longitude = settings.city.longitude,
            timezoneHours = settings.city.timezoneOffsetHours,
            method = settings.calculationMethod,
            asrMethod = settings.asrMethod
        )

        val now = System.currentTimeMillis()
        val next = times.getNextPrayer(now)
        val active = times.getCurrentPrayer(now)
        val hijri = HijriCalendarHelper.getHijriDate(cal, settings.hijriAdjustment)

        _uiState.update {
            it.copy(
                settings = settings,
                prayerTimes = times,
                nextPrayerInfo = next,
                currentPrayer = active,
                hijriDate = hijri
            )
        }
    }

    fun showCopiedToast() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCopiedNotificationVisible = true) }
            delay(2000L)
            _uiState.update { it.copy(isCopiedNotificationVisible = false) }
        }
    }
}
