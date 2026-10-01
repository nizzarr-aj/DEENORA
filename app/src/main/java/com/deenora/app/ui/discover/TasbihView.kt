package com.deenora.app.ui.discover

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deenora.app.data.local.entity.CustomDhikrEntity
import com.deenora.app.ui.i18n.AppLanguage
import com.deenora.app.ui.i18n.AppStrings
import com.deenora.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasbihView(
    viewModel: DiscoverViewModel,
    currentLanguage: AppLanguage,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBackClick() }

    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showAddDhikrDialog by remember { mutableStateOf(false) }
    var showGoalDialog by remember { mutableStateOf(false) }
    var showCustomCountDialog by remember { mutableStateOf(false) }
    var showResetConfirmDialog by remember { mutableStateOf(false) }

    val setProgress = if (state.tasbihTarget > 0) {
        (state.tasbihCount.toFloat() / state.tasbihTarget).coerceIn(0f, 1f)
    } else 0f

    val dailyProgress = if (state.dailyGoal > 0) {
        (state.todayDhikrCount.toFloat() / state.dailyGoal).coerceIn(0f, 1f)
    } else 0f

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scaleOnPress by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 400f),
        label = "tasbih_scale"
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = AppStrings.digitalTasbih(currentLanguage),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("tasbih_back_button")
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Sound Toggle
                    IconButton(
                        onClick = { viewModel.toggleSound() },
                        modifier = Modifier.testTag("tasbih_sound_toggle")
                    ) {
                        Icon(
                            imageVector = if (state.isSoundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                            contentDescription = "Sound",
                            tint = if (state.isSoundEnabled) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Vibration Toggle
                    IconButton(
                        onClick = { viewModel.toggleVibration() },
                        modifier = Modifier.testTag("tasbih_vibrate_toggle")
                    ) {
                        Icon(
                            imageVector = if (state.isVibrationEnabled) Icons.Default.Vibration else Icons.Outlined.Smartphone,
                            contentDescription = "Vibration",
                            tint = if (state.isVibrationEnabled) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Reset Button
                    IconButton(
                        onClick = { showResetConfirmDialog = true },
                        modifier = Modifier.testTag("tasbih_reset_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = "Reset",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Daily Goal Tracking Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("tasbih_daily_goal_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (state.isDailyGoalAchieved) EmeraldPale else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (state.isDailyGoalAchieved) EmeraldGlow else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (state.isDailyGoalAchieved) Icons.Filled.EmojiEvents else Icons.Outlined.TrackChanges,
                                contentDescription = null,
                                tint = if (state.isDailyGoalAchieved) GoldPrimary else EmeraldPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.ARABIC -> "الهدف اليومي للذكر"
                                    AppLanguage.ENGLISH -> "Daily Dhikr Goal"
                                    AppLanguage.FRENCH -> "Objectif Quotidien"
                                },
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (state.isDailyGoalAchieved) EmeraldDark else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Edit Goal Button
                        TextButton(
                            onClick = { showGoalDialog = true },
                            modifier = Modifier.testTag("edit_daily_goal_button")
                        ) {
                            Text(
                                text = "${state.todayDhikrCount} / ${state.dailyGoal}",
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Edit, contentDescription = "Edit Goal", modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { dailyProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (state.isDailyGoalAchieved) GoldPrimary else EmeraldPrimary,
                        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    )

                    AnimatedVisibility(visible = state.isDailyGoalAchieved) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.ARABIC -> "أحسنت! حققت هدف الذكر اليومي بنجاح 🎉"
                                    AppLanguage.ENGLISH -> "Great job! Daily dhikr goal achieved 🎉"
                                    AppLanguage.FRENCH -> "Bravo ! Objectif quotidien atteint 🎉"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDark
                            )
                        }
                    }
                }
            }

            // Dhikr Selector Row with Preset & Custom Dhikrs
            Column(modifier = Modifier.fillMaxWidth()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Presets
                    items(DiscoverUiState.PRESET_DHIKRS) { preset ->
                        val isSelected = preset.key == state.currentDhikr.key && !state.currentDhikr.isCustom
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectDhikr(preset) },
                            label = { Text(preset.arabic, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EmeraldPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }

                    // Custom Dhikrs
                    items(state.customDhikrs) { custom ->
                        val isSelected = state.currentDhikr.isCustom && state.currentDhikr.customId == custom.id
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectCustomDhikr(custom) },
                            label = { Text(custom.arabic, fontWeight = FontWeight.Bold) },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Delete",
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable { viewModel.deleteCustomDhikr(custom) }
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GoldDark,
                                selectedLabelColor = Color.White
                            )
                        )
                    }

                    // Add Custom Dhikr Button
                    item {
                        FilledTonalButton(
                            onClick = { showAddDhikrDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("add_custom_dhikr_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.ARABIC -> "ذكر جديد"
                                    AppLanguage.ENGLISH -> "New Dhikr"
                                    AppLanguage.FRENCH -> "Nouveau"
                                },
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Target Selector (33, 99, 100, 1000, Free, Custom Target)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val targets = listOf(33, 99, 100, 1000, 0)
                    targets.forEach { target ->
                        val isSelected = state.tasbihTarget == target
                        val label = if (target == 0) "Free" else target.toString()
                        Surface(
                            onClick = { viewModel.setTasbihTarget(target) },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) GoldContainer else MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) GoldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                            ),
                            modifier = Modifier.padding(horizontal = 3.dp)
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) GoldOnContainer else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Current Dhikr Display
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = state.currentDhikr.arabic,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldDark,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                val subText = when (currentLanguage) {
                    AppLanguage.ARABIC -> state.currentDhikr.transliteration
                    AppLanguage.ENGLISH -> state.currentDhikr.translationEn
                    AppLanguage.FRENCH -> state.currentDhikr.translationFr
                }
                Text(
                    text = subText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            // Big Interactive Circular Touch Counter with Tactile Ripple & Spring Press
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .scale(scaleOnPress)
                    .clip(CircleShape)
                    .shadow(18.dp, CircleShape)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                EmeraldPrimary,
                                EmeraldDark
                            )
                        )
                    )
                    .border(4.dp, GoldPrimary, CircleShape)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = ripple(bounded = true, color = Color.White)
                    ) {
                        viewModel.tapTasbih(context)
                    }
                    .testTag("tasbih_tap_area"),
                contentAlignment = Alignment.Center
            ) {
                // Circular Progress Ring Indicator
                CircularProgressIndicator(
                    progress = { setProgress },
                    modifier = Modifier.fillMaxSize().padding(10.dp),
                    color = GoldLight,
                    strokeWidth = 7.dp,
                    trackColor = Color.White.copy(alpha = 0.15f)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.tasbihCount.toString(),
                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 62.sp),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    if (state.tasbihTarget > 0) {
                        Text(
                            text = "/ ${state.tasbihTarget}",
                            style = MaterialTheme.typography.titleMedium,
                            color = GoldLight,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "∞ Free",
                            style = MaterialTheme.typography.labelMedium,
                            color = GoldLight
                        )
                    }
                }
            }

            // Set/Jump to Custom Count Button
            TextButton(
                onClick = { showCustomCountDialog = true },
                modifier = Modifier.testTag("set_custom_count_button")
            ) {
                Icon(Icons.Default.Pin, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = when (currentLanguage) {
                        AppLanguage.ARABIC -> "تعديل العداد يدوياً"
                        AppLanguage.ENGLISH -> "Set Custom Count"
                        AppLanguage.FRENCH -> "Définir Compteur"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = EmeraldPrimary
                )
            }

            // Stats Bottom Card: Completed Sets, Today Total, Lifetime Count
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.completedRounds.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.ARABIC -> "الدورات"
                                AppLanguage.ENGLISH -> "Sets"
                                AppLanguage.FRENCH -> "Séries"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(36.dp)
                            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.todayDhikrCount.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = GoldDark
                        )
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.ARABIC -> "تسبيح اليوم"
                                AppLanguage.ENGLISH -> "Today"
                                AppLanguage.FRENCH -> "Aujourd'hui"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(36.dp)
                            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.totalLifetimeCount.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldDark
                        )
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.ARABIC -> "الإجمالي"
                                AppLanguage.ENGLISH -> "Lifetime"
                                AppLanguage.FRENCH -> "Cumulé"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    // --- Dialogs ---

    // 1. Add Custom Dhikr Dialog
    if (showAddDhikrDialog) {
        var newArabic by remember { mutableStateOf("") }
        var newTranslit by remember { mutableStateOf("") }
        var newTarget by remember { mutableStateOf("33") }

        AlertDialog(
            onDismissRequest = { showAddDhikrDialog = false },
            title = {
                Text(
                    text = when (currentLanguage) {
                        AppLanguage.ARABIC -> "إضافة ذكر مخصص"
                        AppLanguage.ENGLISH -> "Add Custom Dhikr"
                        AppLanguage.FRENCH -> "Ajouter un Dhikr"
                    },
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newArabic,
                        onValueChange = { newArabic = it },
                        label = { Text("Arabic Text / الذكر بالعربية") },
                        modifier = Modifier.fillMaxWidth().testTag("custom_dhikr_arabic_input"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = newTranslit,
                        onValueChange = { newTranslit = it },
                        label = { Text("Transliteration / Meaning") },
                        modifier = Modifier.fillMaxWidth().testTag("custom_dhikr_translit_input"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = newTarget,
                        onValueChange = { newTarget = it },
                        label = { Text("Target Count (e.g. 33, 100)") },
                        modifier = Modifier.fillMaxWidth().testTag("custom_dhikr_target_input"),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val targetInt = newTarget.toIntOrNull() ?: 33
                        viewModel.addCustomDhikr(
                            arabic = newArabic,
                            transliteration = newTranslit,
                            translation = newTranslit,
                            target = targetInt
                        )
                        showAddDhikrDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("Save / حفظ")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDhikrDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // 2. Set Custom Count Dialog
    if (showCustomCountDialog) {
        var countInput by remember { mutableStateOf(state.tasbihCount.toString()) }

        AlertDialog(
            onDismissRequest = { showCustomCountDialog = false },
            title = {
                Text(
                    text = when (currentLanguage) {
                        AppLanguage.ARABIC -> "تعديل قيمة العداد"
                        AppLanguage.ENGLISH -> "Set Counter Value"
                        AppLanguage.FRENCH -> "Définir la Valeur"
                    },
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                OutlinedTextField(
                    value = countInput,
                    onValueChange = { countInput = it },
                    label = { Text("Current Count / القيمة الحالية") },
                    modifier = Modifier.fillMaxWidth().testTag("custom_count_input"),
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val num = countInput.toIntOrNull() ?: 0
                        viewModel.setCustomCount(num)
                        showCustomCountDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("Update / تحديث")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomCountDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // 3. Edit Daily Goal Dialog
    if (showGoalDialog) {
        var goalInput by remember { mutableStateOf(state.dailyGoal.toString()) }

        AlertDialog(
            onDismissRequest = { showGoalDialog = false },
            title = {
                Text(
                    text = when (currentLanguage) {
                        AppLanguage.ARABIC -> "تعديل الهدف اليومي للذكر"
                        AppLanguage.ENGLISH -> "Set Daily Dhikr Goal"
                        AppLanguage.FRENCH -> "Définir l'Objectif Quotidien"
                    },
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = goalInput,
                        onValueChange = { goalInput = it },
                        label = { Text("Daily Goal Count (e.g. 300, 500, 1000)") },
                        modifier = Modifier.fillMaxWidth().testTag("daily_goal_input"),
                        singleLine = true
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(100, 300, 500, 1000).forEach { presetGoal ->
                            AssistChip(
                                onClick = { goalInput = presetGoal.toString() },
                                label = { Text(presetGoal.toString()) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val g = goalInput.toIntOrNull() ?: 300
                        viewModel.setDailyGoal(g)
                        showGoalDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("Save Goal / حفظ الهدف")
                }
            },
            dismissButton = {
                TextButton(onClick = { showGoalDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // 4. Reset Confirmation Dialog
    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            title = {
                Text(
                    text = when (currentLanguage) {
                        AppLanguage.ARABIC -> "إعادة تعيين العداد؟"
                        AppLanguage.ENGLISH -> "Reset Counter?"
                        AppLanguage.FRENCH -> "Réinitialiser ?"
                    },
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = when (currentLanguage) {
                        AppLanguage.ARABIC -> "هل تريد تصفير عداد الذكر الحالي؟ لن يتم مسح إجمالي التسبيح."
                        AppLanguage.ENGLISH -> "Do you want to reset the current counter? Your lifetime total will be preserved."
                        AppLanguage.FRENCH -> "Voulez-vous réinitialiser le compteur actuel ? Le total cumulé sera conservé."
                    }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetTasbih()
                        showResetConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Reset / تصفير")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
