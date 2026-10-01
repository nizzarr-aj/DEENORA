package com.deenora.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.deenora.app.data.local.AppDatabase
import com.deenora.app.data.preferences.UserPreferencesRepository
import com.deenora.app.ui.azkar.AzkarScreen
import com.deenora.app.ui.azkar.AzkarViewModel
import com.deenora.app.ui.discover.DiscoverScreen
import com.deenora.app.ui.discover.DiscoverSection
import com.deenora.app.ui.discover.DiscoverViewModel
import com.deenora.app.ui.home.HomeScreen
import com.deenora.app.ui.home.HomeViewModel
import com.deenora.app.ui.i18n.AppLanguage
import com.deenora.app.ui.i18n.AppStrings
import com.deenora.app.ui.profile.ProfileScreen
import com.deenora.app.ui.profile.ProfileViewModel
import com.deenora.app.ui.quran.QuranScreen
import com.deenora.app.ui.quran.QuranViewModel
import com.deenora.app.ui.theme.DeenoraTheme
import com.deenora.app.ui.theme.EmeraldPrimary
import com.deenora.app.ui.theme.GoldPrimary

enum class DeenoraTab(
    val title: (AppLanguage) -> String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    HOME(
        title = { AppStrings.navHome(it) },
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        testTag = "nav_home"
    ),
    QURAN(
        title = { AppStrings.navQuran(it) },
        selectedIcon = Icons.Filled.MenuBook,
        unselectedIcon = Icons.Outlined.MenuBook,
        testTag = "nav_quran"
    ),
    AZKAR(
        title = { AppStrings.navAzkar(it) },
        selectedIcon = Icons.Filled.VolunteerActivism,
        unselectedIcon = Icons.Outlined.VolunteerActivism,
        testTag = "nav_azkar"
    ),
    DISCOVER(
        title = { AppStrings.navDiscover(it) },
        selectedIcon = Icons.Filled.Explore,
        unselectedIcon = Icons.Outlined.Explore,
        testTag = "nav_discover"
    ),
    PROFILE(
        title = { AppStrings.navProfile(it) },
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        testTag = "nav_profile"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeenoraApp(
    database: AppDatabase,
    preferencesRepository: UserPreferencesRepository
) {
    val settings by preferencesRepository.settings.collectAsState()
    val isSystemDark = isSystemInDarkTheme()
    val isDark = settings.isDarkMode ?: isSystemDark

    val layoutDirection = if (settings.language.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    // ViewModels with factory/manual injection
    val homeViewModel = remember { HomeViewModel(preferencesRepository) }
    val quranViewModel = remember { QuranViewModel(database.bookmarkDao(), preferencesRepository) }
    val azkarViewModel = remember { AzkarViewModel(preferencesRepository) }
    val discoverViewModel = remember { DiscoverViewModel(database.tasbihDao(), preferencesRepository) }
    val profileViewModel = remember { ProfileViewModel(preferencesRepository) }

    var currentTab by remember { mutableStateOf(DeenoraTab.HOME) }
    var showLanguageSheet by remember { mutableStateOf(false) }

    DeenoraTheme(darkTheme = isDark) {
        CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp,
                        modifier = Modifier.testTag("deenora_bottom_navigation")
                    ) {
                        DeenoraTab.values().forEach { tab ->
                            val isSelected = currentTab == tab
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = {
                                    currentTab = tab
                                    if (tab == DeenoraTab.DISCOVER) {
                                        discoverViewModel.navigateToSection(DiscoverSection.HUB)
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = tab.title(settings.language)
                                    )
                                },
                                label = {
                                    Text(
                                        text = tab.title(settings.language),
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.surface,
                                    indicatorColor = EmeraldPrimary,
                                    selectedTextColor = EmeraldPrimary,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.testTag(tab.testTag)
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentTab) {
                        DeenoraTab.HOME -> {
                            HomeScreen(
                                viewModel = homeViewModel,
                                onNavigateToQuran = { currentTab = DeenoraTab.QURAN },
                                onNavigateToAzkar = { currentTab = DeenoraTab.AZKAR },
                                onNavigateToQibla = {
                                    discoverViewModel.navigateToSection(DiscoverSection.QIBLA)
                                    currentTab = DeenoraTab.DISCOVER
                                },
                                onNavigateToTasbih = {
                                    discoverViewModel.navigateToSection(DiscoverSection.TASBIH)
                                    currentTab = DeenoraTab.DISCOVER
                                },
                                onLanguageClick = { showLanguageSheet = true }
                            )
                        }
                        DeenoraTab.QURAN -> {
                            QuranScreen(
                                viewModel = quranViewModel,
                                currentLanguage = settings.language,
                                onLanguageClick = { showLanguageSheet = true }
                            )
                        }
                        DeenoraTab.AZKAR -> {
                            AzkarScreen(
                                viewModel = azkarViewModel,
                                currentLanguage = settings.language,
                                onLanguageClick = { showLanguageSheet = true }
                            )
                        }
                        DeenoraTab.DISCOVER -> {
                            DiscoverScreen(
                                viewModel = discoverViewModel,
                                currentLanguage = settings.language,
                                onLanguageClick = { showLanguageSheet = true }
                            )
                        }
                        DeenoraTab.PROFILE -> {
                            ProfileScreen(
                                viewModel = profileViewModel,
                                currentLanguage = settings.language,
                                onLanguageClick = { showLanguageSheet = true }
                            )
                        }
                    }
                }
            }

            // Quick Language Switcher Modal Bottom Sheet
            if (showLanguageSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showLanguageSheet = false }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                            .navigationBarsPadding()
                    ) {
                        Text(
                            text = AppStrings.language(settings.language),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        AppLanguage.values().forEach { lang ->
                            Surface(
                                onClick = {
                                    preferencesRepository.updateLanguage(lang)
                                    showLanguageSheet = false
                                },
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                                color = if (lang == settings.language) EmeraldPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (lang == settings.language) EmeraldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = lang.nativeName,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = lang.titleEn,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    if (lang == settings.language) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = EmeraldPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
