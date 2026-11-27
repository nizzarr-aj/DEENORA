package com.deenora.app.data.prayer

import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.PrayerTimes as AdhanPrayerTimes
import com.batoulapps.adhan.data.DateComponents
import java.util.Calendar

object PrayerTimeCalculator {

    fun calculate(
        year: Int,
        month: Int, // 1..12
        day: Int,
        latitude: Double,
        longitude: Double,
        timezoneHours: Double,
        method: CalculationMethod = CalculationMethod.UMM_AL_QURA,
        asrMethod: AsrJuristicMethod = AsrJuristicMethod.STANDARD
    ): PrayerTimes {
        val coordinates = Coordinates(latitude, longitude)
        val dateComponents = DateComponents(year, month, day)
        val parameters = method.toAdhanParameters(asrMethod)

        val adhanTimes = AdhanPrayerTimes(coordinates, dateComponents, parameters)

        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return PrayerTimes(
            dateMillis = cal.timeInMillis,
            fajrMillis = adhanTimes.fajr.time,
            sunriseMillis = adhanTimes.sunrise.time,
            dhuhrMillis = adhanTimes.dhuhr.time,
            asrMillis = adhanTimes.asr.time,
            maghribMillis = adhanTimes.maghrib.time,
            ishaMillis = adhanTimes.isha.time
        )
    }
}
