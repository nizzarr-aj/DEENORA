package com.deenora.app.ui.home

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deenora.app.data.location.LocationHelper
import com.deenora.app.data.prayer.PrayerType
import com.deenora.app.ui.components.*
import com.deenora.app.ui.i18n.AppLanguage
import com.deenora.app.ui.i18n.AppStrings
import com.deenora.app.ui.theme.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToQuran: () -> Unit,
    onNavigateToAzkar: () -> Unit,
    onNavigateToQibla: () -> Unit,
    onNavigateToTasbih: () -> Unit,
    onLanguageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val lang = state.settings.language

    var showLocationSelector by remember { mutableStateOf(false) }
    var hasRequestedGpsFromBanner by remember { mutableStateOf(false) }

    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(locationPermissionsState.allPermissionsGranted) {
        if (locationPermissionsState.allPermissionsGranted && hasRequestedGpsFromBanner) {
            viewModel.detectLocation(context)
        }
    }

    val gregorianDate = remember(state.currentTimeMillis) {
        val sdf = SimpleDateFormat("EEEE, d MMMM yyyy", when (lang.code) {
            "ar" -> Locale("ar")
            "fr" -> Locale.FRENCH
            else -> Locale.ENGLISH
        })
        sdf.format(Date(state.currentTimeMillis))
    }

    val cityName = when {
        state.settings.city.isGps -> {
            when (lang.code) {
                "ar" -> "📍 ${state.settings.city.nameAr} (GPS)"
                "fr" -> "📍 ${state.settings.city.nameFr} (GPS)"
                else -> "📍 ${state.settings.city.nameEn} (GPS)"
            }
        }
        else -> {
            when (lang.code) {
                "ar" -> state.settings.city.nameAr
                "fr" -> state.settings.city.nameFr
                else -> state.settings.city.nameEn
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            DeenoraTopBar(
                title = AppStrings.appName(lang),
                subtitle = cityName,
                currentLanguage = lang,
                onLanguageClick = onLanguageClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
        ) {
            // Greeting & Dates Header with Location change button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = AppStrings.greeting(lang),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = gregorianDate,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Location Pill Button
                    Surface(
                        onClick = { showLocationSelector = true },
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.35f)),
                        modifier = Modifier.testTag("home_location_picker_pill")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = if (state.settings.city.isGps) Icons.Default.GpsFixed else Icons.Outlined.LocationOn,
                                contentDescription = "Change Location",
                                tint = if (state.settings.city.isGps) EmeraldPrimary else GoldDark,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = when (lang) {
                                    AppLanguage.ARABIC -> "تغيير المدينة"
                                    AppLanguage.ENGLISH -> "Change City"
                                    AppLanguage.FRENCH -> "Changer ville"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Optional GPS Prompt Banner if not currently on GPS
            if (!state.settings.city.isGps) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("gps_suggestion_card")
                            .clickable {
                                if (locationPermissionsState.allPermissionsGranted) {
                                    viewModel.detectLocation(context)
                                } else {
                                    hasRequestedGpsFromBanner = true
                                    locationPermissionsState.launchMultiplePermissionRequest()
                                }
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = EmeraldPale.copy(alpha = 0.6f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.25f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MyLocation,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = when (lang) {
                                            AppLanguage.ARABIC -> "تفعيل الموقع التلقائي (GPS)"
                                            AppLanguage.ENGLISH -> "Use Device Real Location"
                                            AppLanguage.FRENCH -> "Utiliser le GPS de l'appareil"
                                        },
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldDark
                                    )
                                    Text(
                                        text = when (lang) {
                                            AppLanguage.ARABIC -> "حساب مواقيت الصلاة بدقة حسب مكانك الفعلي"
                                            AppLanguage.ENGLISH -> "Calculate prayer times from your exact location"
                                            AppLanguage.FRENCH -> "Horaires calculés selon votre position réelle"
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            if (state.isLocationDetecting) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = EmeraldPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Next Prayer Hero Card with live ticker countdown
            item {
                val times = state.prayerTimes
                val nextInfo = state.nextPrayerInfo
                if (times != null && nextInfo != null) {
                    NextPrayerHeroCard(
                        nextPrayerInfo = nextInfo,
                        prayerTimes = times,
                        currentLanguage = lang,
                        hijriDateFormatted = state.hijriDate.format(lang),
                        cityName = cityName,
                        onLocationClick = { showLocationSelector = true }
                    )
                }
            }

            // Quick Access Bar
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = AppStrings.quickAccess(lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickActionButton(
                            title = AppStrings.navQuran(lang),
                            icon = Icons.Outlined.MenuBook,
                            color = EmeraldPrimary,
                            onClick = onNavigateToQuran,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionButton(
                            title = AppStrings.tasbih(lang),
                            icon = Icons.Outlined.FilterVintage,
                            color = GoldPrimary,
                            onClick = onNavigateToTasbih,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionButton(
                            title = AppStrings.qibla(lang),
                            icon = Icons.Outlined.Explore,
                            color = Color(0xFF2A7261),
                            onClick = onNavigateToQibla,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionButton(
                            title = AppStrings.navAzkar(lang),
                            icon = Icons.Outlined.VolunteerActivism,
                            color = Color(0xFFC07025),
                            onClick = onNavigateToAzkar,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Today's 5 Prayer Times Section (Calculated via Adhan library)
            item {
                val times = state.prayerTimes
                if (times != null) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = AppStrings.todayPrayers(lang),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = state.settings.calculationMethod.titleEn,
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldDark,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        val prayers = listOf(
                            PrayerType.FAJR,
                            PrayerType.SUNRISE,
                            PrayerType.DHUHR,
                            PrayerType.ASR,
                            PrayerType.MAGHRIB,
                            PrayerType.ISHA
                        )

                        prayers.forEach { prayer ->
                            val isActive = state.currentPrayer == prayer
                            val isNext = state.nextPrayerInfo?.prayer == prayer
                            PrayerCardItem(
                                prayer = prayer,
                                timeFormatted = times.getFormattedTime(prayer),
                                isActive = isActive,
                                isNext = isNext,
                                language = lang,
                                onSoundToggle = {
                                    // toggle adhan audio preview
                                }
                            )
                        }
                    }
                }
            }

            // Daily Dua Card
            item {
                val dua = state.dailyDua
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("daily_dua_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.35f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(GoldContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.AutoAwesome,
                                        contentDescription = null,
                                        tint = GoldDark,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Text(
                                    text = AppStrings.dailyDua(lang),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Row {
                                IconButton(
                                    onClick = {
                                        copyToClipboard(context, "${dua.arabicText}\n${dua.translationEn}")
                                        viewModel.showCopiedToast()
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.ContentCopy,
                                        contentDescription = "Copy",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        shareContent(context, "${dua.arabicText}\n\n${dua.translationEn}\n(${dua.source})")
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Share,
                                        contentDescription = "Share",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = dua.arabicText,
                            style = MaterialTheme.typography.titleLarge.copy(lineHeight = 36.sp),
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        val translated = when (lang) {
                            AppLanguage.ARABIC -> dua.transliteration
                            AppLanguage.ENGLISH -> dua.translationEn
                            AppLanguage.FRENCH -> dua.translationFr
                        }
                        Text(
                            text = translated,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = dua.source,
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldDark,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Daily Quran Verse Card
            item {
                val verse = state.dailyVerse
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("daily_verse_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(EmeraldPale),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.MenuBook,
                                        contentDescription = null,
                                        tint = EmeraldDark,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Text(
                                    text = AppStrings.dailyVerse(lang),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Row {
                                IconButton(
                                    onClick = {
                                        copyToClipboard(context, "${verse.textAr}\n${verse.textEn}")
                                        viewModel.showCopiedToast()
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.ContentCopy,
                                        contentDescription = "Copy",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        shareContent(context, "${verse.textAr}\n\n${verse.textEn}\n[Surah ${verse.surahNumber}:${verse.ayahNumber}]")
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Share,
                                        contentDescription = "Share",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = verse.textAr,
                            style = MaterialTheme.typography.titleLarge.copy(lineHeight = 36.sp),
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            color = EmeraldDark,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        val verseTranslation = when (lang) {
                            AppLanguage.ARABIC -> verse.tafsirSummary
                            AppLanguage.ENGLISH -> verse.textEn
                            AppLanguage.FRENCH -> verse.textFr
                        }
                        Text(
                            text = verseTranslation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "[${verse.surahNumber}:${verse.ayahNumber}]",
                            style = MaterialTheme.typography.labelSmall,
                            color = EmeraldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Daily Islamic Reminder / Hadith
            item {
                val reminder = state.dailyReminder
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("daily_reminder_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Lightbulb,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = AppStrings.islamicReminder(lang),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = reminder.arabicText,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        val trans = when (lang) {
                            AppLanguage.ARABIC -> ""
                            AppLanguage.ENGLISH -> reminder.translationEn
                            AppLanguage.FRENCH -> reminder.translationFr
                        }
                        if (trans.isNotEmpty()) {
                            Text(
                                text = trans,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        Text(
                            text = reminder.narrator,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }

    // Location Selector Dialog
    if (showLocationSelector) {
        LocationSelectorDialog(
            currentCity = state.settings.city,
            currentLanguage = lang,
            onCitySelected = { city ->
                viewModel.setCity(city)
            },
            onDismissRequest = { showLocationSelector = false }
        )
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("DEENORA", text)
    clipboard.setPrimaryClip(clip)
}

private fun shareContent(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Share via"))
}
