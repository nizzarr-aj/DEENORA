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
            val today = Calendar.getInstance()
            val cityName = when (settings.language.code) {
                "ar" -> settings.city.nameAr
                "fr" -> settings.city.nameFr
                else -> settings.city.nameEn
            }

            // Schedule every remaining prayer for today and every prayer for tomorrow.
            // This avoids a missing Isha/overnight gap if a worker is delayed by the OS.
            for (dayOffset in 0..1) {
                val date = (today.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, dayOffset) }
                val prayers = PrayerTimeCalculator.calculate(
                    year = date.get(Calendar.YEAR),
                    month = date.get(Calendar.MONTH) + 1,
                    day = date.get(Calendar.DAY_OF_MONTH),
                    latitude = settings.city.latitude,
                    longitude = settings.city.longitude,
                    timezoneHours = settings.city.timezoneOffsetHours,
                    method = settings.calculationMethod,
                    asrMethod = settings.asrMethod
                )

                val prayerTimes = listOf(
                    PrayerType.FAJR to prayers.fajrMillis,
                    PrayerType.DHUHR to prayers.dhuhrMillis,
                    PrayerType.ASR to prayers.asrMillis,
                    PrayerType.MAGHRIB to prayers.maghribMillis,
                    PrayerType.ISHA to prayers.ishaMillis
                )

                prayerTimes.forEach { (prayer, timeMillis) ->
                    if (timeMillis > now) {
                        val prayerName = prayer.getDisplayName(settings.language)
                        val notifId = 100 + prayer.ordinal
                        val inputData = Data.Builder()
                            .putString(PrayerAlarmWorker.KEY_PRAYER_NAME, prayerName)
                            .putString(PrayerAlarmWorker.KEY_CITY_NAME, cityName)
                            .putInt(PrayerAlarmWorker.KEY_NOTIFICATION_ID, notifId)
                            .build()
                        val request = OneTimeWorkRequestBuilder<PrayerAlarmWorker>()
                            .setInitialDelay(timeMillis - now, TimeUnit.MILLISECONDS)
                            .setInputData(inputData)
                            .addTag(PRAYER_WORK_TAG)
                            .addTag("prayer_${prayer.name.lowercase()}")
                            .build()
                        val uniqueName = "prayer_${prayer.name.lowercase()}_${timeMillis}"
                        workManager.enqueueUniqueWork(uniqueName, ExistingWorkPolicy.REPLACE, request)
                    }
                }
            }
        }
    }

    fun cancelAllPrayerNotifications(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(PRAYER_WORK_TAG)
    }
}
