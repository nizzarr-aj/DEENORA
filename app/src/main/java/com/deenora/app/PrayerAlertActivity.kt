package com.deenora.app

import android.os.Build
import android.os.Bundle
import android.content.Intent
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deenora.app.service.StopPrayerAlertReceiver

class PrayerAlertActivity : ComponentActivity() {
    companion object {
        const val EXTRA_PRAYER_NAME = "alert_prayer_name"
        const val EXTRA_CITY_NAME = "alert_city_name"
        const val EXTRA_NOTIFICATION_ID = "alert_notification_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val prayer = intent.getStringExtra(EXTRA_PRAYER_NAME) ?: "الصلاة"
        val city = intent.getStringExtra(EXTRA_CITY_NAME).orEmpty()
        val id = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 1001)
        setContent {
            MaterialTheme {
                Box(Modifier.fillMaxSize().background(
                    Brush.verticalGradient(listOf(Color(0xFF33495F), Color(0xFF89959D), Color(0xFF1B3048)))
                ).padding(24.dp)) {
                    IconButton(onClick = { finish() }, modifier = Modifier.align(Alignment.TopStart)) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = Color.White)
                    }
                    Column(Modifier.align(Alignment.Center).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("دِينك", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(40.dp))
                        Surface(shape = RoundedCornerShape(160.dp), color = Color.White.copy(alpha = 0.17f)) {
                            Column(Modifier.padding(horizontal = 36.dp, vertical = 44.dp),
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("حان الآن موعد صلاة", color = Color.White, fontSize = 20.sp, textAlign = TextAlign.Center)
                                Spacer(Modifier.height(14.dp))
                                Text(prayer, color = Color.White, fontSize = 42.sp, fontWeight = FontWeight.Bold)
                                if (city.isNotBlank()) {
                                    Spacer(Modifier.height(8.dp))
                                    Text(city, color = Color.White.copy(alpha = 0.9f), fontSize = 16.sp)
                                }
                            }
                        }
                    }
                    Button(onClick = {
                        sendBroadcast(Intent(this@PrayerAlertActivity, StopPrayerAlertReceiver::class.java)
                            .putExtra(StopPrayerAlertReceiver.EXTRA_NOTIFICATION_ID, id))
                        finish()
                    }, modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(58.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF078B57))) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null)
                        Text("  إيقاف الأذان", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
