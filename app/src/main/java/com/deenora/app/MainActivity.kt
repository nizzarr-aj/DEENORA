package com.deenora.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.deenora.app.data.local.AppDatabase
import com.deenora.app.data.preferences.UserPreferencesRepository
import com.deenora.app.service.PrayerNotificationHelper
import com.deenora.app.service.PrayerWorkManagerScheduler
import com.deenora.app.ui.DeenoraApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val preferencesRepository = UserPreferencesRepository(applicationContext)

        // Initialize Notification Channel and schedule prayer alerts via WorkManager
        PrayerNotificationHelper.createNotificationChannel(applicationContext)
        PrayerWorkManagerScheduler.scheduleAllPrayerNotifications(applicationContext)

        setContent {
            DeenoraApp(
                database = database,
                preferencesRepository = preferencesRepository
            )
        }
    }
}
