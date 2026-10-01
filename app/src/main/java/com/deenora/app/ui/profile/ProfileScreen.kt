package com.deenora.app.ui.profile

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deenora.app.data.prayer.AsrJuristicMethod
import com.deenora.app.data.prayer.CalculationMethod
import com.deenora.app.data.prayer.CityLocation
import com.deenora.app.data.prayer.PredefinedCities
import com.deenora.app.ui.components.DeenoraTopBar
import com.deenora.app.ui.components.LocationSelectorDialog
import com.deenora.app.ui.i18n.AppLanguage
import com.deenora.app.ui.i18n.AppStrings
import com.deenora.app.ui.theme.*
import android.os.Build
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    currentLanguage: AppLanguage,
    onLanguageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)
    } else null

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showCityDialog by remember { mutableStateOf(false) }
    var showMethodDialog by remember { mutableStateOf(false) }
    var showAsrDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            DeenoraTopBar(
                title = AppStrings.navProfile(currentLanguage),
                subtitle = AppStrings.settings(currentLanguage),
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
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
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
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(
                                        Brush.linearGradient(listOf(EmeraldMedium, EmeraldPrimary))
                                    )
                                    .border(2.dp, GoldLight, RoundedCornerShape(18.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "D",
                                    color = GoldLight,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 32.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "DEENORA • دينورا",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                text = AppStrings.appVersion(currentLanguage),
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldLight
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = AppStrings.settings(currentLanguage),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            item {
                SettingsOptionItem(
                    title = AppStrings.language(currentLanguage),
                    value = currentLanguage.nativeName,
                    icon = Icons.Outlined.Translate,
                    onClick = { showLanguageDialog = true },
                    tag = "setting_language_row"
                )
            }

            item {
                val currentCityName = when (currentLanguage) {
                    AppLanguage.ARABIC -> state.settings.city.nameAr
                    AppLanguage.FRENCH -> state.settings.city.nameFr
                    AppLanguage.ENGLISH -> state.settings.city.nameEn
                }
                SettingsOptionItem(
                    title = AppStrings.locationCity(currentLanguage),
                    value = currentCityName,
                    icon = Icons.Outlined.LocationOn,
                    onClick = { showCityDialog = true },
                    tag = "setting_city_row"
                )
            }

            item {
                val methodName = when (currentLanguage) {
                    AppLanguage.ARABIC -> state.settings.calculationMethod.titleAr
                    AppLanguage.FRENCH -> state.settings.calculationMethod.titleFr
                    AppLanguage.ENGLISH -> state.settings.calculationMethod.titleEn
                }
                SettingsOptionItem(
                    title = AppStrings.calculationMethod(currentLanguage),
                    value = methodName,
                    icon = Icons.Outlined.Schedule,
                    onClick = { showMethodDialog = true },
                    tag = "setting_method_row"
                )
            }

            item {
                val asrName = when (state.settings.asrMethod) {
                    AsrJuristicMethod.STANDARD -> AppStrings.standardShafii(currentLanguage)
                    AsrJuristicMethod.HANAFI -> AppStrings.hanafi(currentLanguage)
                }
                SettingsOptionItem(
                    title = AppStrings.asrMethod(currentLanguage),
                    value = asrName,
                    icon = Icons.Outlined.AccessTime,
                    onClick = { showAsrDialog = true },
                    tag = "setting_asr_row"
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(GoldContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.DarkMode,
                                    contentDescription = null,
                                    tint = GoldDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = AppStrings.hijriAdjustment(currentLanguage),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${AppStrings.days(currentLanguage, state.settings.hijriAdjustment)} days",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    val curr = state.settings.hijriAdjustment
                                    if (curr > -2) viewModel.setHijriAdjustment(curr - 1)
                                },
                                enabled = state.settings.hijriAdjustment > -2
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease")
                            }

                            Text(
                                text = AppStrings.days(currentLanguage, state.settings.hijriAdjustment),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp)
                            )

                            IconButton(
                                onClick = {
                                    val curr = state.settings.hijriAdjustment
                                    if (curr < 2) viewModel.setHijriAdjustment(curr + 1)
                                },
                                enabled = state.settings.hijriAdjustment < 2
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase")
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Brightness4,
                                    contentDescription = null,
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Text(
                                    text = AppStrings.darkMode(currentLanguage),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Switch(
                                checked = state.settings.isDarkMode == true,
                                onCheckedChange = { isChecked -> viewModel.setDarkMode(isChecked) }
                            )
                        }

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Notifications,
                                    contentDescription = null,
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Text(
                                    text = AppStrings.notifications(currentLanguage),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Switch(
                                checked = state.settings.isNotificationEnabled,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        if (notificationPermissionState != null && !notificationPermissionState.status.isGranted) {
                                            notificationPermissionState.launchPermissionRequest()
                                        }
                                        viewModel.setNotification(true, context)
                                    } else {
                                        viewModel.setNotification(false, context)
                                    }
                                }
                            )
                        }

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Vibration,
                                    contentDescription = null,
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Text(
                                    text = AppStrings.vibrationFeedback(currentLanguage),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Switch(
                                checked = state.settings.isVibrationEnabled,
                                onCheckedChange = { isChecked -> viewModel.setVibration(isChecked) }
                            )
                        }
                    }
                }
            }

            item {
                SettingsOptionItem(
                    title = when (currentLanguage) {
                        AppLanguage.ARABIC -> "سياسة الخصوصية"
                        AppLanguage.FRENCH -> "Politique de confidentialité"
                        AppLanguage.ENGLISH -> "Privacy Policy"
                    },
                    value = "DEENORA",
                    icon = Icons.Outlined.Security,
                    onClick = { PrivacyPolicyHelper.open(context) },
                    tag = "setting_privacy_policy_row"
                )
            }

            item {
                SettingsOptionItem(
                    title = AppStrings.aboutApp(currentLanguage),
                    value = "DEENORA v1.0.0",
                    icon = Icons.Outlined.Info,
                    onClick = { showAboutDialog = true },
                    tag = "setting_about_row"
                )
            }
        }
    }

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(AppStrings.language(currentLanguage)) },
            text = {
                Column {
                    AppLanguage.values().forEach { lang ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setLanguage(lang)
                                    showLanguageDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${lang.nativeName} (${lang.titleEn})",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (lang == currentLanguage) FontWeight.Bold else FontWeight.Normal
                            )
                            if (lang == currentLanguage) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldPrimary)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showCityDialog) {
        LocationSelectorDialog(
            currentCity = state.settings.city,
            currentLanguage = currentLanguage,
            onCitySelected = { city ->
                viewModel.setCity(city, context)
            },
            onDismissRequest = { showCityDialog = false }
        )
    }

    if (showMethodDialog) {
        AlertDialog(
            onDismissRequest = { showMethodDialog = false },
            title = { Text(AppStrings.calculationMethod(currentLanguage)) },
            text = {
                LazyColumn {
                    items(CalculationMethod.values()) { method ->
                        val name = when (currentLanguage) {
                            AppLanguage.ARABIC -> method.titleAr
                            AppLanguage.FRENCH -> method.titleFr
                            AppLanguage.ENGLISH -> method.titleEn
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setCalculationMethod(method, context)
                                    showMethodDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (method == state.settings.calculationMethod) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.weight(1f)
                            )
                            if (method == state.settings.calculationMethod) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldPrimary)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMethodDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showAsrDialog) {
        AlertDialog(
            onDismissRequest = { showAsrDialog = false },
            title = { Text(AppStrings.asrMethod(currentLanguage)) },
            text = {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.setAsrMethod(AsrJuristicMethod.STANDARD, context)
                                showAsrDialog = false
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = AppStrings.standardShafii(currentLanguage),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (state.settings.asrMethod == AsrJuristicMethod.STANDARD) FontWeight.Bold else FontWeight.Normal
                        )
                        if (state.settings.asrMethod == AsrJuristicMethod.STANDARD) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldPrimary)
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.setAsrMethod(AsrJuristicMethod.HANAFI, context)
                                showAsrDialog = false
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = AppStrings.hanafi(currentLanguage),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (state.settings.asrMethod == AsrJuristicMethod.HANAFI) FontWeight.Bold else FontWeight.Normal
                        )
                        if (state.settings.asrMethod == AsrJuristicMethod.HANAFI) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldPrimary)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAsrDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("DEENORA (دينورا)", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "DEENORA is a pure, calm, and elegant Islamic companion built with high fidelity native Android Jetpack Compose.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Features: Real-time prayer calculations, Hijri calendar, Quran with Uthmani script and audio recitations, authentic Azkar, Qibla compass with magnetic sensor integration, digital Tasbih, and 99 Names of Allah.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) { Text("Close") }
            }
        )
    }
}

@Composable
fun SettingsOptionItem(
    title: String,
    value: String,
    icon: ImageVector,
    onClick: () -> Unit,
    tag: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag(tag)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(EmeraldPale),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = EmeraldDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
