package com.deenora.app.ui.discover

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deenora.app.data.calendar.IslamicCalendarRepository
import com.deenora.app.data.prayer.HijriCalendarHelper
import com.deenora.app.ui.i18n.AppLanguage
import com.deenora.app.ui.i18n.AppStrings
import com.deenora.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IslamicCalendarView(
    currentLanguage: AppLanguage,
    hijriAdjustment: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBackClick() }

    val todayHijri = HijriCalendarHelper.getHijriDate(adjustmentDays = hijriAdjustment)
    var expandedEvent by remember { mutableIntStateOf(-1) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = AppStrings.islamicCalendar(currentLanguage),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("calendar_back_button")
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Current Hijri Month Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldDark)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(EmeraldDark, EmeraldPrimary)
                                )
                            )
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Outlined.CalendarMonth,
                                contentDescription = null,
                                tint = GoldLight,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = todayHijri.format(currentLanguage),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Islamic Lunar Calendar (Umm al-Qura)",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                        }
                    }
                }
            }

            // Holy Occasions Title
            item {
                Text(
                    text = when (currentLanguage) {
                        AppLanguage.ARABIC -> "أبرز المناسبات والأعياد الإسلامية"
                        AppLanguage.ENGLISH -> "Major Islamic Events & Holidays"
                        AppLanguage.FRENCH -> "Événements & Fêtes Islamiques Majeurs"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Events List
            items(IslamicCalendarRepository.EVENTS.size) { eventIndex ->
                val event = IslamicCalendarRepository.EVENTS[eventIndex]
                val expanded = expandedEvent == eventIndex
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { expandedEvent = if (expanded) -1 else eventIndex },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(GoldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = event.hijriDay.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldDark
                                )
                                Text(
                                    text = "M${event.hijriMonth}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                    color = GoldDark
                                )
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = event.getTitle(currentLanguage),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = event.getDescription(currentLanguage),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                            if (expanded) {
                                Spacer(Modifier.height(10.dp))
                                HorizontalDivider(color = GoldPrimary.copy(alpha = 0.35f))
                                Spacer(Modifier.height(8.dp))
                                Text(text = when (currentLanguage) {
                                    AppLanguage.ARABIC -> "التاريخ الهجري: ${event.hijriDay} ${listOf("محرم","صفر","ربيع الأول","ربيع الآخر","جمادى الأولى","جمادى الآخرة","رجب","شعبان","رمضان","شوال","ذو القعدة","ذو الحجة")[event.hijriMonth - 1]}"
                                    AppLanguage.ENGLISH -> "Hijri date: ${event.hijriDay} ${listOf("Muharram","Safar","Rabi' al-Awwal","Rabi' al-Thani","Jumada al-Awwal","Jumada al-Thani","Rajab","Sha'ban","Ramadan","Shawwal","Dhu al-Qi'dah","Dhu al-Hijjah")[event.hijriMonth - 1]}"
                                    AppLanguage.FRENCH -> "Date hégirienne : ${event.hijriDay} ${listOf("Mouharram","Safar","Rabi' al-Awwal","Rabi' al-Thani","Joumada al-Oula","Joumada ath-Thania","Rajab","Cha'bane","Ramadan","Chawwal","Dhou al-Qi'da","Dhou al-Hijja")[event.hijriMonth - 1]}"
                                }, color = EmeraldDark, fontWeight = FontWeight.Bold)
                                Text(text = when (currentLanguage) {
                                    AppLanguage.ARABIC -> "قد يختلف التاريخ الميلادي المقابل حسب رؤية الهلال والبلد."
                                    AppLanguage.ENGLISH -> "The corresponding Gregorian date may vary by local moon sighting and country."
                                    AppLanguage.FRENCH -> "La date grégorienne correspondante peut varier selon l'observation locale du croissant et le pays."
                                }, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            } else Text(when (currentLanguage) {
                                AppLanguage.ARABIC -> "اضغط لمعرفة التفاصيل"
                                AppLanguage.ENGLISH -> "Tap to see details"
                                AppLanguage.FRENCH -> "Touchez pour les détails"
                            }, color = EmeraldDark, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }
    }
}
