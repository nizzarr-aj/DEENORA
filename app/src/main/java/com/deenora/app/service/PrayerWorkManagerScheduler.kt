package com.deenora.app.service

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.deenora.app.data.prayer.PrayerTimeCalculator
import com.deenora.app.data.prayer.PrayerType
import com.deenora.app.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

object PrayerWorkManagerScheduler {

    const val PRAYER_WORK_TAG = "deenora_prayer_alarm_tag"

    fun scheduleAllPrayerNotifications(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val prefs = UserPreferencesRepository(context)
            val settings = prefs.settings.first()

            val workManager = WorkManager.getInstance(context)

            if (!settings.isNotificationEnabled) {
                workManager.cancelAllWorkByTag(PRAYER_WORK_TAG)
                return@launch
            }

            val now = System.currentTimeMillis()
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH) + 1
            val day = cal.get(Calendar.DAY_OF_MONTH)

            // Calculate Today's Prayers
            val todayPrayers = PrayerTimeCalculator.calculate(
                year = year,
                month = month,
                day = day,
                latitude = settings.city.latitude,
                longitude = settings.city.longitude,
                timezoneHours = settings.city.timezoneOffsetHours,
                method = settings.calculationMethod,
                asrMethod = settings.asrMethod
            )

            // Calculate Tomorrow's Prayers (so next day Fajr is pre-scheduled)
            val tomorrowCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }
            val tomorrowPrayers = PrayerTimeCalculator.calculate(
                year = tomorrowCal.get(Calendar.YEAR),
                month = tomorrowCal.get(Calendar.MONTH) + 1,
                day = tomorrowCal.get(Calendar.DAY_OF_MONTH),
                latitude = settings.city.latitude,
                longitude = settings.city.longitude,
                timezoneHours = settings.city.timezoneOffsetHours,
                method = settings.calculationMethod,
                asrMethod = settings.asrMethod
            )

            val cityName = when (settings.language.code) {
                "ar" -> settings.city.nameAr
                "fr" -> settings.city.nameFr
                else -> settings.city.nameEn
            }

            val prayersToSchedule = listOf(
                Pair(PrayerType.FAJR, todayPrayers.fajrMillis),
                Pair(PrayerType.DHUHR, todayPrayers.dhuhrMillis),
                Pair(PrayerType.ASR, todayPrayers.asrMillis),
                Pair(PrayerType.MAGHRIB, todayPrayers.maghribMillis),
                Pair(PrayerType.ISHA, todayPrayers.ishaMillis),
                // Tomorrow
                Pair(PrayerType.FAJR, tomorrowPrayers.fajrMillis),
                Pair(PrayerType.DHUHR, tomorrowPrayers.dhuhrMillis)
            )

            prayersToSchedule.forEach { (prayer, timeMillis) ->
                if (timeMillis > now) {
                    val delayMillis = timeMillis - now
                    val prayerName = prayer.getDisplayName(settings.language)
                    val notifId = 100 + prayer.ordinal

                    val inputData = Data.Builder()
                        .putString(PrayerAlarmWorker.KEY_PRAYER_NAME, prayerName)
                        .putString(PrayerAlarmWorker.KEY_CITY_NAME, cityName)
                        .putInt(PrayerAlarmWorker.KEY_NOTIFICATION_ID, notifId)
                        .build()

                    val workRequest = OneTimeWorkRequestBuilder<PrayerAlarmWorker>()
                        .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                        .setInputData(inputData)
                        .addTag(PRAYER_WORK_TAG)
                        .addTag("prayer_${prayer.name.lowercase()}")
                        .build()

                    val uniqueWorkName = "prayer_${prayer.name.lowercase()}_$timeMillis"
                    workManager.enqueueUniqueWork(
                        uniqueWorkName,
                        ExistingWorkPolicy.REPLACE,
                        workRequest
                    )
                }
            }
        }
    }

    fun cancelAllPrayerNotifications(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(PRAYER_WORK_TAG)
    }
}
