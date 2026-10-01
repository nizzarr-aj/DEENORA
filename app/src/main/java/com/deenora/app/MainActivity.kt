package com.deenora.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deenora.app.ads.AdMobConsentManager
import com.deenora.app.ads.DeenoraBannerAd
import com.deenora.app.data.local.AppDatabase
import com.deenora.app.data.preferences.UserPreferencesRepository
import com.deenora.app.service.PrayerNotificationHelper
import com.deenora.app.service.PrayerWorkManagerScheduler
import com.deenora.app.ui.DeenoraApp
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val preferencesRepository =
            UserPreferencesRepository(applicationContext)

        PrayerNotificationHelper.createNotificationChannel(
            applicationContext
        )

        PrayerWorkManagerScheduler.scheduleAllPrayerNotifications(
            applicationContext
        )

        setContent {

            LaunchedEffect(Unit) {

                AdMobConsentManager.requestConsentAndInitialize(
                    activity = this@MainActivity,
                    onAdsReady = {
                        MobileAds.initialize(this@MainActivity)
                    }
                )
            }

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {

                    DeenoraApp(
                        database = database,
                        preferencesRepository = preferencesRepository
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(top = 4.dp)
                ) {

                    DeenoraBannerAd()
                }
            }
        }
    }
}
