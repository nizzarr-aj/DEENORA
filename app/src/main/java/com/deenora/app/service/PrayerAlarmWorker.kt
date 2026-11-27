package com.deenora.app.service

import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
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

        if (!settings.isNotificationEnabled) return Result.success()

        // WorkManager may retry, or a stale scheduled alarm may survive an app update.
        // This per-day guard prevents duplicate adhan playback for the same prayer.
        val dayKey = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
        val guardKey = "fired_${dayKey}_${notifId}_${cityName.hashCode()}"
        val guard = applicationContext.getSharedPreferences("prayer_alarm_guard", Context.MODE_PRIVATE)
        if (guard.getBoolean(guardKey, false)) {
            PrayerWorkManagerScheduler.scheduleAllPrayerNotifications(applicationContext)
            return Result.success()
        }

        // Commit before audio starts so simultaneous workers cannot play duplicate adhans.
        if (!guard.edit().putBoolean(guardKey, true).commit()) return Result.retry()

        // Schedule future prayers immediately; the current worker can remain foreground
        // for the duration of the full MP3 without delaying tomorrow's alarms.
        PrayerWorkManagerScheduler.scheduleAllPrayerNotifications(applicationContext)

        val notification = PrayerNotificationHelper.buildPrayerNotification(
            context = applicationContext,
            prayerName = prayerName,
            cityName = cityName,
            notificationId = notifId
        )
        val foregroundInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(notifId, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
        } else {
            ForegroundInfo(notifId, notification)
        }

        return try {
            setForeground(foregroundInfo)
            // Play the bundled MP3 locally, once, from start to finish. No streaming.
            AdhanAudioPlayer.playAndWait(applicationContext)
            Result.success()
        } catch (_: kotlinx.coroutines.CancellationException) {
            throw kotlinx.coroutines.CancellationException("Adhan playback cancelled")
        } catch (_: Exception) {
            Result.failure()
        }
    }
}
