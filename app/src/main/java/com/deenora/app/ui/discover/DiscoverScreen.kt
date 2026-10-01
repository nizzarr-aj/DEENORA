package com.deenora.app.ui.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deenora.app.ui.components.DeenoraTopBar
import com.deenora.app.ui.i18n.AppLanguage
import com.deenora.app.ui.i18n.AppStrings
import com.deenora.app.ui.theme.*

@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel,
    currentLanguage: AppLanguage,
    onLanguageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    when (state.currentSection) {
        DiscoverSection.QIBLA -> {
            QiblaCompassView(
                viewModel = viewModel,
                currentLanguage = currentLanguage,
                onBackClick = { viewModel.navigateToSection(DiscoverSection.HUB) }
            )
        }
        DiscoverSection.TASBIH -> {
            TasbihView(
                viewModel = viewModel,
                currentLanguage = currentLanguage,
                onBackClick = { viewModel.navigateToSection(DiscoverSection.HUB) }
            )
        }
        DiscoverSection.NAMES_OF_ALLAH -> {
            NamesOfAllahView(
                viewModel = viewModel,
                currentLanguage = currentLanguage,
                onBackClick = { viewModel.navigateToSection(DiscoverSection.HUB) }
            )
        }
        DiscoverSection.CALENDAR -> {
            IslamicCalendarView(
                currentLanguage = currentLanguage,
                hijriAdjustment = state.settings.hijriAdjustment,
                onBackClick = { viewModel.navigateToSection(DiscoverSection.HUB) }
            )
        }
        DiscoverSection.HUB -> {
            Scaffold(
                modifier = modifier.fillMaxSize(),
                topBar = {
                    DeenoraTopBar(
                        title = AppStrings.navDiscover(currentLanguage),
                        subtitle = "Qibla • Tasbih • 99 Names • Calendar",
                        currentLanguage = currentLanguage,
                        onLanguageClick = onLanguageClick
                    )
                }
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        DiscoverCardItem(
                            title = AppStrings.qiblaCompass(currentLanguage),
                            description = when (currentLanguage) {
                                AppLanguage.ARABIC -> "بوصلة دقيقة لتحديد اتجاه الكعبة المشرفة في مكة المكرمة مع المسافة"
                                AppLanguage.ENGLISH -> "Accurate compass pointing directly to the Kaaba with live distance in km"
                                AppLanguage.FRENCH -> "Boussole précise indiquant la direction de la Kaaba et la distance"
                            },
                            icon = Icons.Outlined.Explore,
                            accentColor = EmeraldPrimary,
                            tag = "discover_qibla_card",
                            onClick = { viewModel.navigateToSection(DiscoverSection.QIBLA) }
                        )
                    }

                    item {
                        DiscoverCardItem(
                            title = AppStrings.digitalTasbih(currentLanguage),
                            description = when (currentLanguage) {
                                AppLanguage.ARABIC -> "سبحة ذكية مع ردود فعل لمسية وأذكار مأثورة وسجل تسبيح متواصل"
                                AppLanguage.ENGLISH -> "Digital bead counter with tactile haptics, preset dhikrs, and lifetime history"
                                AppLanguage.FRENCH -> "Chapelet interactif avec vibrations haptiques et historique"
                            },
                            icon = Icons.Outlined.FilterVintage,
                            accentColor = GoldPrimary,
                            tag = "discover_tasbih_card",
                            onClick = { viewModel.navigateToSection(DiscoverSection.TASBIH) }
                        )
                    }

                    item {
                        DiscoverCardItem(
                            title = AppStrings.namesOfAllah(currentLanguage),
                            description = when (currentLanguage) {
                                AppLanguage.ARABIC -> "الأسماء الحسنى التسعة والتسعون مع معانيها وتأملاتها الروحية"
                                AppLanguage.ENGLISH -> "99 Beautiful Names of Allah with transliteration, meanings, and reflections"
                                AppLanguage.FRENCH -> "Les 99 Plus Beaux Noms d'Allah avec significations et réflexions"
                            },
                            icon = Icons.Outlined.AutoAwesome,
                            accentColor = Color(0xFF236858),
                            tag = "discover_names_card",
                            onClick = { viewModel.navigateToSection(DiscoverSection.NAMES_OF_ALLAH) }
                        )
                    }

                    item {
                        DiscoverCardItem(
                            title = AppStrings.islamicCalendar(currentLanguage),
                            description = when (currentLanguage) {
                                AppLanguage.ARABIC -> "مواعيد المناسبات الدينية والأعياد وأيام الصيام في التقويم الهجري"
                                AppLanguage.ENGLISH -> "Islamic Hijri calendar dates, holy occasions, Ramadan, and Eid dates"
                                AppLanguage.FRENCH -> "Dates du calendrier hégirien, fêtes religieuses et événements sacrés"
                            },
                            icon = Icons.Outlined.CalendarMonth,
                            accentColor = Color(0xFFB57022),
                            tag = "discover_calendar_card",
                            onClick = { viewModel.navigateToSection(DiscoverSection.CALENDAR) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DiscoverCardItem(
    title: String,
    description: String,
    icon: ImageVector,
    accentColor: Color,
    tag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag(tag)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }

            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = accentColor
            )
        }
    }
}
