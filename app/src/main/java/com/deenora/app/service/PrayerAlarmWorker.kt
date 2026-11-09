package com.deenora.app.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.deenora.app.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
            // WorkManager can retry or an older scheduled alarm can survive an update.
            // Persist a per-prayer/per-day guard so the same prayer never alerts twice.
            val dayKey = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
            val guardKey = "fired_${dayKey}_${notifId}_${cityName.hashCode()}"
            val guard = applicationContext.getSharedPreferences(
                "prayer_alarm_guard",
                Context.MODE_PRIVATE
            )
            val alreadyAlerted = guard.getBoolean(guardKey, false)

            if (!alreadyAlerted) {
                // Commit before showing the alert to prevent simultaneous duplicate workers.
                val saved = guard.edit().putBoolean(guardKey, true).commit()
                if (saved) {
                    PrayerNotificationHelper.showPrayerNotification(
                        context = applicationContext,
                        prayerName = prayerName,
                        cityName = cityName,
                        notificationId = notifId
                    )
                }
            }

            // Reschedule subsequent upcoming prayer times even when a duplicate was skipped.
            PrayerWorkManagerScheduler.scheduleAllPrayerNotifications(applicationContext)
        }

        return Result.success()
    }
}
