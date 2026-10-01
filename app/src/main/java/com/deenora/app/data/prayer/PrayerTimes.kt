package com.deenora.app.data.prayer

import com.deenora.app.ui.i18n.AppLanguage
import com.deenora.app.ui.i18n.AppStrings
import java.util.Calendar
import java.util.Locale
import kotlin.math.*

enum class PrayerType {
    FAJR,
    SUNRISE,
    DHUHR,
    ASR,
    MAGHRIB,
    ISHA;

    fun getDisplayName(language: AppLanguage): String = when (this) {
        FAJR -> AppStrings.prayerFajr(language)
        SUNRISE -> AppStrings.prayerSunrise(language)
        DHUHR -> AppStrings.prayerDhuhr(language)
        ASR -> AppStrings.prayerAsr(language)
        MAGHRIB -> AppStrings.prayerMaghrib(language)
        ISHA -> AppStrings.prayerIsha(language)
    }
}

data class NextPrayerInfo(
    val prayer: PrayerType,
    val prayerTimeMillis: Long,
    val remainingMillis: Long,
    val progress: Float
)

data class PrayerTimes(
    val dateMillis: Long,
    val fajrMillis: Long,
    val sunriseMillis: Long,
    val dhuhrMillis: Long,
    val asrMillis: Long,
    val maghribMillis: Long,
    val ishaMillis: Long
) {
    fun getTimeFor(prayer: PrayerType): Long = when (prayer) {
        PrayerType.FAJR -> fajrMillis
        PrayerType.SUNRISE -> sunriseMillis
        PrayerType.DHUHR -> dhuhrMillis
        PrayerType.ASR -> asrMillis
        PrayerType.MAGHRIB -> maghribMillis
        PrayerType.ISHA -> ishaMillis
    }

    fun getFormattedTime(prayer: PrayerType): String {
        val millis = getTimeFor(prayer)
        val cal = Calendar.getInstance().apply { timeInMillis = millis }
        val hours = cal.get(Calendar.HOUR_OF_DAY)
        val minutes = cal.get(Calendar.MINUTE)
        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes)
    }

    fun getNextPrayer(now: Long): NextPrayerInfo {
        val prayers = listOf(
            PrayerType.FAJR to fajrMillis,
            PrayerType.SUNRISE to sunriseMillis,
            PrayerType.DHUHR to dhuhrMillis,
            PrayerType.ASR to asrMillis,
            PrayerType.MAGHRIB to maghribMillis,
            PrayerType.ISHA to ishaMillis
        )

        for (i in prayers.indices) {
            val (prayer, time) = prayers[i]
            if (now < time) {
                val prevTime = if (i == 0) fajrMillis - 8 * 3600 * 1000L else prayers[i - 1].second
                val totalWindow = (time - prevTime).coerceAtLeast(1L)
                val elapsed = (now - prevTime).coerceAtLeast(0L)
                val progress = (elapsed.toFloat() / totalWindow).coerceIn(0f, 1f)
                return NextPrayerInfo(
                    prayer = prayer,
                    prayerTimeMillis = time,
                    remainingMillis = time - now,
                    progress = progress
                )
            }
        }

        // After Isha: next is tomorrow's Fajr (approx + 24h Fajr)
        val nextFajr = fajrMillis + 24 * 3600 * 1000L
        val totalWindow = (nextFajr - ishaMillis).coerceAtLeast(1L)
        val elapsed = (now - ishaMillis).coerceAtLeast(0L)
        val progress = (elapsed.toFloat() / totalWindow).coerceIn(0f, 1f)
        return NextPrayerInfo(
            prayer = PrayerType.FAJR,
            prayerTimeMillis = nextFajr,
            remainingMillis = (nextFajr - now).coerceAtLeast(0L),
            progress = progress
        )
    }

    fun getCurrentPrayer(now: Long): PrayerType {
        return when {
            now < fajrMillis -> PrayerType.ISHA
            now < sunriseMillis -> PrayerType.FAJR
            now < dhuhrMillis -> PrayerType.SUNRISE
            now < asrMillis -> PrayerType.DHUHR
            now < maghribMillis -> PrayerType.ASR
            now < ishaMillis -> PrayerType.MAGHRIB
            else -> PrayerType.ISHA
        }
    }
}
