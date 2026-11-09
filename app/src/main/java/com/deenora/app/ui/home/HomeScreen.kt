package com.deenora.app.ui.home

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import com.deenora.app.R
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deenora.app.data.location.LocationHelper
import com.deenora.app.data.prayer.PrayerType
import com.deenora.app.data.quran.QuranRepository
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
    var previewRingtone by remember { mutableStateOf<Ringtone?>(null) }
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
        topBar = {},
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            DeenkNightBackdrop()
            LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
        ) {
            // Full-width branded header inspired by the approved Deenk reference.
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        onClick = { showLocationSelector = true },
                        shape = RoundedCornerShape(24.dp),
                        color = Color(0xD9081D3B),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldLight),
                        modifier = Modifier.testTag("home_location_picker_pill")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.LocationOn, null, tint = GoldLight, modifier = Modifier.size(18.dp))
                            Text(cityName, color = Color.White, style = MaterialTheme.typography.labelMedium, maxLines = 1)
                            Icon(Icons.Default.ArrowDropDown, null, tint = GoldLight)
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(AppStrings.appName(lang), color = GoldLight, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                            Text(
                                text = when (lang) {
                                    AppLanguage.ARABIC -> "رفيقك في دينك وحياتك"
                                    AppLanguage.ENGLISH -> "Your companion in faith and life"
                                    AppLanguage.FRENCH -> "Votre compagnon de foi"
                                },
                                color = Color.White.copy(alpha = 0.9f),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                        Box(
                            modifier = Modifier.size(58.dp).clip(RoundedCornerShape(18.dp))
                                .background(Brush.linearGradient(listOf(Color(0xFF173D70), Color(0xFF071A36))))
                                .border(1.5.dp, GoldLight, RoundedCornerShape(18.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.material3.Icon(
                                painter = painterResource(id = R.drawable.ic_deenora_logo_vector),
                                contentDescription = "Deenk",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(54.dp)
                            )
                        }
                    }
                }
                Text(
                    text = gregorianDate,
                    color = Color.White.copy(alpha = 0.82f),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                    textAlign = TextAlign.End
                )
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

            // Featured Quran banner, matching the illustrated reference card.
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().height(154.dp).clickable(onClick = onNavigateToQuran).testTag("home_quran_feature"),
                    shape = RoundedCornerShape(26.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldLight),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize().background(
                            Brush.horizontalGradient(listOf(Color(0xFF071A36), Color(0xFF17477C), Color(0xFF071A36)))
                        ).padding(16.dp)
                    ) {
                        Icon(Icons.Outlined.MenuBook, null, tint = GoldLight.copy(alpha = 0.95f), modifier = Modifier.align(Alignment.CenterStart).size(92.dp))
                        Column(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            Text(
                                text = when (lang) {
                                    AppLanguage.ARABIC -> "القرآن الكريم"
                                    AppLanguage.ENGLISH -> "The Holy Quran"
                                    AppLanguage.FRENCH -> "Le Saint Coran"
                                },
                                color = GoldLight,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = when (lang) {
                                    AppLanguage.ARABIC -> "استمع وتدبّر آيات الله العظيمة"
                                    AppLanguage.ENGLISH -> "Listen and reflect on the verses"
                                    AppLanguage.FRENCH -> "Écoutez et méditez les versets"
                                },
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Surface(
                                onClick = onNavigateToQuran,
                                color = GoldLight,
                                shape = RoundedCornerShape(24.dp)
                            ) {
                                Text(
                                    text = when (lang) {
                                        AppLanguage.ARABIC -> "ابدأ القراءة  ▶"
                                        AppLanguage.ENGLISH -> "Start reading  ▶"
                                        AppLanguage.FRENCH -> "Commencer  ▶"
                                    },
                                    color = Color(0xFF102342),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Quick Access: spacious two-column tiles for comfortable one-hand use.
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = AppStrings.quickAccess(lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
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
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickActionButton(
                            title = AppStrings.qibla(lang),
                            icon = Icons.Outlined.Explore,
                            color = EmeraldLight,
                            onClick = onNavigateToQibla,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionButton(
                            title = AppStrings.navAzkar(lang),
                            icon = Icons.Outlined.VolunteerActivism,
                            color = GoldDark,
                            onClick = onNavigateToAzkar,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Compact five-prayer timetable in a single pearl card.
            item {
                val times = state.prayerTimes
                if (times != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("today_prayer_strip"),
                        shape = RoundedCornerShape(26.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF5E5)),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldPrimary)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(AppStrings.todayPrayers(lang), color = Color(0xFF102342), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                                Text(state.settings.calculationMethod.titleEn, color = GoldDark, style = MaterialTheme.typography.labelSmall)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                listOf(PrayerType.FAJR, PrayerType.DHUHR, PrayerType.ASR, PrayerType.MAGHRIB, PrayerType.ISHA).forEach { prayer ->
                                    val active = state.nextPrayerInfo?.prayer == prayer
                                    Column(
                                        modifier = Modifier.weight(1f).clip(RoundedCornerShape(15.dp))
                                            .background(if (active) Color(0xFF102D55) else Color(0xFFFFEED2))
                                            .border(1.dp, if (active) GoldPrimary else Color(0xFFE5CFA8), RoundedCornerShape(15.dp))
                                            .padding(vertical = 12.dp, horizontal = 3.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Icon(
                                            imageVector = when (prayer) {
                                                PrayerType.FAJR -> Icons.Outlined.Brightness4
                                                PrayerType.DHUHR -> Icons.Outlined.WbSunny
                                                PrayerType.ASR -> Icons.Outlined.WbTwilight
                                                PrayerType.MAGHRIB -> Icons.Outlined.NightsStay
                                                else -> Icons.Filled.DarkMode
                                            },
                                            contentDescription = null,
                                            tint = if (active) GoldLight else GoldDark,
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Text(
                                            prayer.getDisplayName(lang),
                                            color = if (active) Color.White else Color(0xFF102342),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1
                                        )
                                        Text(
                                            times.getFormattedTime(prayer),
                                            color = if (active) GoldLight else Color(0xFF17213A),
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
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
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7EA)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.65f))
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
                            color = Color(0xFF102D55),
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

                        val surahName = QuranRepository.SURAHS
                            .firstOrNull { it.number == verse.surahNumber }
                            ?.getName(lang).orEmpty()
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = EmeraldPale,
                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.55f))
                        ) {
                            Text(
                                text = "${surahName} • ${verse.surahNumber}:${verse.ayahNumber}",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = EmeraldDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
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
                viewModel.setCity(city, context)
            },
            onDismissRequest = { showLocationSelector = false }
        )
    }
}

}

@Composable
private fun DeenkNightBackdrop() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        drawRect(
            brush = Brush.verticalGradient(
                colorStops = arrayOf(
                    0f to Color(0xFF04152F),
                    0.20f to Color(0xFF0A2B55),
                    0.39f to Color(0xFF173E69),
                    0.43f to Color(0xFFF5EDE2),
                    1f to Color(0xFFF7F4EF)
                ),
                startY = 0f,
                endY = height
            )
        )
        // Fine stars across the deep-blue header.
        for (i in 0 until 115) {
            val x = ((i * 73) % 997) / 997f * width
            val y = ((i * 47) % 389) / 389f * height * 0.39f
            val radius = if (i % 9 == 0) 2.0f else 0.8f
            drawCircle(Color(0xFFFFE6AD).copy(alpha = if (i % 4 == 0) 0.9f else 0.48f), radius, androidx.compose.ui.geometry.Offset(x, y))
        }
        // Crescent moon with a soft golden edge.
        val moon = androidx.compose.ui.geometry.Offset(width * 0.20f, height * 0.115f)
        drawCircle(Color(0xFFFFD989), radius = width * 0.075f, center = moon)
        drawCircle(Color(0xFF0A2B55), radius = width * 0.069f, center = androidx.compose.ui.geometry.Offset(moon.x + width * 0.032f, moon.y - height * 0.012f))
        // Distant mosque skyline along the transition into the pearl content area.
        val base = height * 0.405f
        drawRect(Color(0xFF061B37), topLeft = androidx.compose.ui.geometry.Offset(0f, base), size = androidx.compose.ui.geometry.Size(width, height * 0.045f))
        drawRect(Color(0xFF0A2446), topLeft = androidx.compose.ui.geometry.Offset(width * 0.12f, base - height * 0.075f), size = androidx.compose.ui.geometry.Size(width * 0.76f, height * 0.075f))
        drawCircle(Color(0xFF0A2446), radius = width * 0.105f, center = androidx.compose.ui.geometry.Offset(width * 0.5f, base - height * 0.075f))
        drawRect(Color(0xFF0A2446), topLeft = androidx.compose.ui.geometry.Offset(width * 0.46f, base - height * 0.16f), size = androidx.compose.ui.geometry.Size(width * 0.08f, height * 0.085f))
        drawCircle(Color(0xFF0A2446), radius = width * 0.04f, center = androidx.compose.ui.geometry.Offset(width * 0.5f, base - height * 0.16f))
        for (x in listOf(0.12f, 0.82f)) {
            drawRect(Color(0xFF0A2446), topLeft = androidx.compose.ui.geometry.Offset(width * x, base - height * 0.14f), size = androidx.compose.ui.geometry.Size(width * 0.045f, height * 0.14f))
            drawCircle(Color(0xFF0A2446), radius = width * 0.023f, center = androidx.compose.ui.geometry.Offset(width * (x + 0.022f), base - height * 0.14f))
        }
        // Warm horizon glow.
        drawRect(Brush.verticalGradient(listOf(Color(0x00F5D69A), Color(0x55F5D69A))), topLeft = androidx.compose.ui.geometry.Offset(0f, base - height * 0.025f), size = androidx.compose.ui.geometry.Size(width, height * 0.075f))
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
