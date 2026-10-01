package com.deenora.app.ui.quran

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deenora.app.data.quran.Ayah
import com.deenora.app.data.quran.RevelationType
import com.deenora.app.data.quran.Surah
import com.deenora.app.ui.i18n.AppLanguage
import com.deenora.app.ui.i18n.AppStrings
import com.deenora.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahDetailScreen(
    surah: Surah,
    verses: List<Ayah>,
    fontSizeSp: Float,
    isAudioPlaying: Boolean,
    playingAyahIndex: Int,
    bookmarkedAyahNumbers: Set<Int>,
    currentLanguage: AppLanguage,
    onBackClick: () -> Unit,
    onToggleBookmark: (Ayah) -> Unit,
    onToggleAudio: () -> Unit,
    onFontSizeChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBackClick() }

    val context = LocalContext.current
    var showFontSizeDialog by remember { mutableStateOf(false) }
    var selectedAyahForTafsir by remember { mutableStateOf<Ayah?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = surah.getName(currentLanguage),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${surah.versesCount} verses • " +
                                    if (surah.revelationType == RevelationType.MAKKI) AppStrings.makki(currentLanguage)
                                    else AppStrings.madani(currentLanguage),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("surah_detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showFontSizeDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.FormatSize,
                            contentDescription = "Font size",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            // Audio Recitation Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 6.dp,
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (isAudioPlaying) GoldPrimary else EmeraldPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isAudioPlaying) Icons.Filled.Headphones else Icons.Outlined.Headphones,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (isAudioPlaying) "Recitation Playing..." else "Recitation (Mishary Alafasy)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            if (isAudioPlaying && verses.isNotEmpty()) {
                                Text(
                                    text = "Ayah ${verses.getOrNull(playingAyahIndex)?.ayahNumber ?: 1} of ${surah.versesCount}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    FilledIconButton(
                        onClick = onToggleAudio,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = EmeraldPrimary
                        ),
                        modifier = Modifier.testTag("audio_play_pause_button")
                    ) {
                        Icon(
                            imageVector = if (isAudioPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = if (isAudioPlaying) "Pause" else "Play"
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Surah Header Banner
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
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = surah.nameAr,
                                style = MaterialTheme.typography.displayMedium,
                                color = GoldLight,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = surah.getName(currentLanguage) + if (surah.getMeaning(currentLanguage).isNotEmpty()) " (${surah.getMeaning(currentLanguage)})" else "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.8f)
                            )

                            if (surah.number != 9) { // At-Tawbah does not begin with Bismillah
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = AppStrings.bismillah(currentLanguage),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = GoldLight,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Ayahs List
            itemsIndexed(verses) { index, ayah ->
                val isPlayingCurrent = isAudioPlaying && playingAyahIndex == index
                val isBookmarked = bookmarkedAyahNumbers.contains(ayah.ayahNumber)

                AyahCard(
                    ayah = ayah,
                    fontSizeSp = fontSizeSp,
                    isPlaying = isPlayingCurrent,
                    isBookmarked = isBookmarked,
                    currentLanguage = currentLanguage,
                    onBookmarkClick = { onToggleBookmark(ayah) },
                    onCopyClick = {
                        val clip = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clip.setPrimaryClip(ClipData.newPlainText("Ayah", "${ayah.textAr}\n${ayah.textEn}"))
                    },
                    onShareClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "${ayah.textAr}\n\n${ayah.textEn}\n[${surah.nameEn} ${surah.number}:${ayah.ayahNumber}]")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Ayah"))
                    },
                    onTafsirClick = { selectedAyahForTafsir = ayah }
                )
            }
        }
    }

    // Font Size Dialog
    if (showFontSizeDialog) {
        AlertDialog(
            onDismissRequest = { showFontSizeDialog = false },
            title = { Text(AppStrings.fontSize(currentLanguage)) },
            text = {
                Column {
                    Text(
                        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                        fontSize = fontSizeSp.sp,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                    )
                    Slider(
                        value = fontSizeSp,
                        onValueChange = onFontSizeChange,
                        valueRange = 18f..36f,
                        steps = 8
                    )
                    Text(
                        text = "${fontSizeSp.toInt()} sp",
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showFontSizeDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    // Tafsir / Ayah Insight Dialog
    selectedAyahForTafsir?.let { ayah ->
        AlertDialog(
            onDismissRequest = { selectedAyahForTafsir = null },
            title = {
                Text("Ayah Insight [${surah.number}:${ayah.ayahNumber}]", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = ayah.textAr,
                        style = MaterialTheme.typography.titleMedium,
                        color = EmeraldPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Divider(color = GoldPrimary.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 6.dp))
                    Text(
                        text = ayah.textEn,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (ayah.tafsirSummary.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Reflection: ${ayah.tafsirSummary}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedAyahForTafsir = null }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun AyahCard(
    ayah: Ayah,
    fontSizeSp: Float,
    isPlaying: Boolean,
    isBookmarked: Boolean,
    currentLanguage: AppLanguage,
    onBookmarkClick: () -> Unit,
    onCopyClick: () -> Unit,
    onShareClick: () -> Unit,
    onTafsirClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("ayah_card_${ayah.ayahNumber}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlaying) EmeraldPale else MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isPlaying) EmeraldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Ayah Header: Number Badge and Action Icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ornamental Ayah Number Badge
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(GoldContainer)
                        .border(1.dp, GoldPrimary.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ayah.ayahNumber.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = GoldOnContainer
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBookmarkClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = onCopyClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ContentCopy,
                            contentDescription = "Copy",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = onShareClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = onTafsirClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Tafsir",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Arabic Quranic Text
            Text(
                text = ayah.textAr,
                fontSize = fontSizeSp.sp,
                lineHeight = (fontSizeSp * 1.8f).sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Right,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Translation
            val translation = when (currentLanguage) {
                AppLanguage.ARABIC -> ayah.tafsirSummary
                AppLanguage.ENGLISH -> ayah.textEn
                AppLanguage.FRENCH -> ayah.textFr
            }
            if (translation.isNotEmpty()) {
                Text(
                    text = translation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )
            }
        }
    }
}
