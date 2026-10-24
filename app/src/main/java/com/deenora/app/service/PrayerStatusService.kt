package com.deenora.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.deenora.app.MainActivity
import com.deenora.app.R
import com.deenora.app.data.preferences.UserPreferencesRepository
import com.deenora.app.data.prayer.PrayerTimeCalculator
import com.deenora.app.data.prayer.PrayerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PrayerStatusService : Service() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val channelId = "deenora_prayer_status"
    private val notificationId = 7401
    private var refreshJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        createStatusChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(notificationId, buildNotification("DEENORA", "جاري تحديث مواقيت الصلاة…"))
        refreshJob?.cancel()
        refreshJob = scope.launch {
            while (true) {
                try {
                    val settings = UserPreferencesRepository(applicationContext).settings.first()
                    if (!settings.isNotificationEnabled) {
                        stopForeground(STOP_FOREGROUND_REMOVE)
                        stopSelf()
                        return@launch
                    }
                    val cal = Calendar.getInstance()
                    val today = PrayerTimeCalculator.calculate(
                        year = cal.get(Calendar.YEAR), month = cal.get(Calendar.MONTH) + 1,
                        day = cal.get(Calendar.DAY_OF_MONTH), latitude = settings.city.latitude,
                        longitude = settings.city.longitude, timezoneHours = settings.city.timezoneOffsetHours,
                        method = settings.calculationMethod, asrMethod = settings.asrMethod
                    )
                    val prayers = listOf(
                        PrayerType.FAJR to today.fajrMillis,
                        PrayerType.DHUHR to today.dhuhrMillis,
                        PrayerType.ASR to today.asrMillis,
                        PrayerType.MAGHRIB to today.maghribMillis,
                        PrayerType.ISHA to today.ishaMillis
                    )
                    val next = prayers.firstOrNull { it.second > System.currentTimeMillis() }
                    val city = when (settings.language.code) {
                        "ar" -> settings.city.nameAr
                        "fr" -> settings.city.nameFr
                        else -> settings.city.nameEn
                    }
                    val nextText = if (next != null) {
                        val name = next.first.getDisplayName(settings.language)
                        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(next.second))
                        "الصلاة القادمة: $name • $time"
                    } else "مواقيت الصلاة • DEENORA"
                    val title = "DEENORA • $city"
                    val body = if (settings.language.code == "ar") nextText else
                        if (next != null) "Next: ${next.first.getDisplayName(settings.language)} • " +
                            SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(next.second))
                        else "Prayer times • DEENORA"
                    getSystemService(NotificationManager::class.java).notify(notificationId, buildNotification(title, body))
                } catch (_: Exception) {
                    // Keep the ongoing notification alive if a temporary calculation fails.
                }
                delay(15 * 60 * 1000L)
            }
        }
        return START_STICKY
    }

    private fun createStatusChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "DEENORA — مواقيت الصلاة", NotificationManager.IMPORTANCE_LOW)
            channel.description = "مواقيت الصلاة والمدينة في الإشعارات"
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private fun buildNotification(title: String, body: String): Notification {
        val pending = PendingIntent.getActivity(
            this, 7401, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setContentIntent(pending)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun onDestroy() {
        refreshJob?.cancel()
        scope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, PrayerStatusService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) context.startForegroundService(intent)
            else context.startService(intent)
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, PrayerStatusService::class.java))
        }
    }
}
