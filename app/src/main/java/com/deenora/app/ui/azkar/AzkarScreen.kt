package com.deenora.app.ui.azkar

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deenora.app.data.azkar.ZikrCategory
import com.deenora.app.data.azkar.ZikrItem
import com.deenora.app.ui.components.DeenoraTopBar
import com.deenora.app.ui.i18n.AppLanguage
import com.deenora.app.ui.i18n.AppStrings
import com.deenora.app.ui.theme.*

@Composable
fun AzkarScreen(
    viewModel: AzkarViewModel,
    currentLanguage: AppLanguage,
    onLanguageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val totalItems = state.items.size
    val completedCount = state.items.count { state.completedItems.contains(it.id) }
    val progressFraction = if (totalItems > 0) completedCount.toFloat() / totalItems else 0f

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            DeenoraTopBar(
                title = AppStrings.navAzkar(currentLanguage),
                subtitle = state.selectedCategory.getTitle(currentLanguage),
                currentLanguage = currentLanguage,
                onLanguageClick = onLanguageClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Category Scrollable Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ZikrCategory.values()) { category ->
                    val isSelected = category == state.selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectCategory(category) },
                        label = {
                            Text(
                                text = category.getTitle(currentLanguage),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EmeraldPrimary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("azkar_chip_${category.name.lowercase()}")
                    )
                }
            }

            // Category Completion Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "$completedCount / $totalItems ${AppStrings.completed(currentLanguage)}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { progressFraction },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = EmeraldPrimary,
                            trackColor = MaterialTheme.colorScheme.surface
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    TextButton(
                        onClick = { viewModel.resetAllCurrentCategory() },
                        modifier = Modifier.testTag("reset_all_azkar_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = "Reset All",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(AppStrings.reset(currentLanguage), style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            // Azkar Items List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(state.items, key = { it.id }) { item ->
                    val currentCount = state.counts[item.id] ?: 0
                    val isDone = state.completedItems.contains(item.id)

                    ZikrItemCard(
                        item = item,
                        currentCount = currentCount,
                        isDone = isDone,
                        currentLanguage = currentLanguage,
                        onIncrement = { viewModel.incrementCount(item, context) },
                        onReset = { viewModel.resetItem(item) },
                        onShare = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "${item.arabicText}\n\n${item.translationEn}\n(${item.reference})")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Zikr"))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ZikrItemCard(
    item: ZikrItem,
    currentCount: Int,
    isDone: Boolean,
    currentLanguage: AppLanguage,
    onIncrement: () -> Unit,
    onReset: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("zikr_card_${item.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDone) EmeraldPale.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isDone) EmeraldPrimary.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Action & Reference Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = GoldContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = item.reference,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = GoldDark,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(onClick = onReset, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = "Reset",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Arabic text with tashkeel
            Text(
                text = item.arabicText,
                style = MaterialTheme.typography.titleLarge.copy(lineHeight = 36.sp),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Right,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Translation / Transliteration
            val textContent = when (currentLanguage) {
                AppLanguage.ARABIC -> item.transliteration
                AppLanguage.ENGLISH -> item.translationEn
                AppLanguage.FRENCH -> item.translationFr
            }
            if (textContent.isNotEmpty()) {
                Text(
                    text = textContent,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Virtue / Benefit
            val virtueText = when (currentLanguage) {
                AppLanguage.ARABIC -> item.virtueAr
                AppLanguage.ENGLISH -> item.virtueEn
                AppLanguage.FRENCH -> item.virtueFr
            }
            if (virtueText.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Stars,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = virtueText,
                        style = MaterialTheme.typography.bodySmall,
                        color = GoldDark,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Big Interactive Circular Counter Button
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    onClick = onIncrement,
                    shape = RoundedCornerShape(28.dp),
                    color = if (isDone) EmeraldPrimary else GoldContainer,
                    border = androidx.compose.foundation.BorderStroke(
                        2.dp,
                        if (isDone) EmeraldGlow else GoldPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(56.dp)
                        .testTag("increment_zikr_${item.id}")
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isDone) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Completed",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${item.targetCount} / ${item.targetCount}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = "$currentCount / ${item.targetCount}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = GoldDark
                            )
                        }
                    }
                }
            }
        }
    }
}
