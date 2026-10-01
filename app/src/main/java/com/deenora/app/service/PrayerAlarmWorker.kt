package com.deenora.app.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.deenora.app.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.first

class PrayerAlarmWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val KEY_PRAYER_NAME = "prayer_name"
        const val KEY_CITY_NAME = "city_name"
        const val KEY_NOTIFICATION_ID = "notification_id"
    }

    override suspend fun doWork(): Result {
        val prayerName = inputData.getString(KEY_PRAYER_NAME) ?: "Prayer"
        val cityName = inputData.getString(KEY_CITY_NAME) ?: "Your Location"
        val notifId = inputData.getInt(KEY_NOTIFICATION_ID, 1001)

        val prefs = UserPreferencesRepository(applicationContext)
        val settings = prefs.settings.first()

        if (settings.isNotificationEnabled) {
            PrayerNotificationHelper.showPrayerNotification(
                context = applicationContext,
                prayerName = prayerName,
                cityName = cityName,
                notificationId = notifId
            )

            // Reschedule subsequent upcoming prayer times
            PrayerWorkManagerScheduler.scheduleAllPrayerNotifications(applicationContext)
        }

        return Result.success()
    }
}
