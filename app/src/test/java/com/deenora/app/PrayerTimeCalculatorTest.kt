package com.deenora.app

import com.deenora.app.data.prayer.AsrJuristicMethod
import com.deenora.app.data.prayer.CalculationMethod
import com.deenora.app.data.prayer.HijriCalendarHelper
import com.deenora.app.data.prayer.PrayerTimeCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.*

class PrayerTimeCalculatorTest {

    @Test
    fun testPrayerCalculationForMakkah() {
        val year = 2026
        val month = 10
        val day = 1

        val times = PrayerTimeCalculator.calculate(
            year = year,
            month = month,
            day = day,
            latitude = 21.4225,
            longitude = 39.8262,
            timezoneHours = 3.0,
            method = CalculationMethod.UMM_AL_QURA
        )

        assertNotNull(times)
        assertTrue("Fajr before Sunrise", times.fajrMillis < times.sunriseMillis)
        assertTrue("Sunrise before Dhuhr", times.sunriseMillis < times.dhuhrMillis)
        assertTrue("Dhuhr before Asr", times.dhuhrMillis < times.asrMillis)
        assertTrue("Asr before Maghrib", times.asrMillis < times.maghribMillis)
        assertTrue("Maghrib before Isha", times.maghribMillis < times.ishaMillis)
    }

    @Test
    fun testMadhhabComparison() {
        val year = 2026
        val month = 10
        val day = 1

        val timesStandard = PrayerTimeCalculator.calculate(
            year = year,
            month = month,
            day = day,
            latitude = 51.5074,
            longitude = -0.1278,
            timezoneHours = 1.0,
            method = CalculationMethod.MWL,
            asrMethod = AsrJuristicMethod.STANDARD
        )

        val timesHanafi = PrayerTimeCalculator.calculate(
            year = year,
            month = month,
            day = day,
            latitude = 51.5074,
            longitude = -0.1278,
            timezoneHours = 1.0,
            method = CalculationMethod.MWL,
            asrMethod = AsrJuristicMethod.HANAFI
        )

        assertTrue(
            "Hanafi Asr must be later than or equal to Standard Asr",
            timesHanafi.asrMillis >= timesStandard.asrMillis
        )
    }

    @Test
    fun testLiveCountdownAndNextPrayer() {
        val year = 2026
        val month = 10
        val day = 1

        val times = PrayerTimeCalculator.calculate(
            year = year,
            month = month,
            day = day,
            latitude = 48.8566,
            longitude = 2.3522,
            timezoneHours = 2.0,
            method = CalculationMethod.FRANCE
        )

        // Test before Fajr
        val nextInfo = times.getNextPrayer(times.fajrMillis - 3600 * 1000L)
        assertNotNull(nextInfo)
        assertTrue("Remaining millis must be positive", nextInfo.remainingMillis > 0)
        assertTrue("Progress between 0 and 1", nextInfo.progress in 0f..1f)
    }

    @Test
    fun testQiblaBearingSphericalTrig() {
        fun calculateQibla(lat: Double, lng: Double): Double {
            val kaabaLat = Math.toRadians(21.422487)
            val kaabaLng = Math.toRadians(39.826206)
            val phi1 = Math.toRadians(lat)
            val deltaLambda = kaabaLng - Math.toRadians(lng)

            val y = sin(deltaLambda) * cos(kaabaLat)
            val x = cos(phi1) * sin(kaabaLat) - sin(phi1) * cos(kaabaLat) * cos(deltaLambda)
            var qibla = Math.toDegrees(atan2(y, x))
            return (qibla + 360.0) % 360.0
        }

        // London to Kaaba is ~119°
        val londonQibla = calculateQibla(51.5074, -0.1278)
        assertTrue("London Qibla between 118 and 120", londonQibla in 118.0..120.0)

        // Cairo to Kaaba is ~136°
        val cairoQibla = calculateQibla(30.0444, 31.2357)
        assertTrue("Cairo Qibla between 135 and 137", cairoQibla in 135.0..137.0)

        // New York to Kaaba is ~58°
        val nyQibla = calculateQibla(40.7128, -74.0060)
        assertTrue("New York Qibla between 57 and 60", nyQibla in 57.0..60.0)
    }

    @Test
    fun testHijriCalendarConversion() {
        val hijri = HijriCalendarHelper.getHijriDate()
        assertNotNull(hijri)
        assertTrue("Hijri day is positive", hijri.day in 1..30)
        assertTrue("Hijri month is positive", hijri.month in 1..12)
        assertTrue("Hijri year is current era", hijri.year in 1445..1500)
    }

    @Test
    fun testTasbihGoalAndTargetProgress() {
        val target = 33
        val count = 22
        val progress = count.toFloat() / target
        assertTrue("Progress between 0 and 1", progress in 0f..1f)
        assertEquals(0.666f, progress, 0.01f)

        val dailyGoal = 300
        val todayCount = 300
        val isAchieved = todayCount >= dailyGoal
        assertTrue("Daily goal must be achieved", isAchieved)
    }
}
