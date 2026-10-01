package com.deenora.app.ui.quran

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deenora.app.data.local.entity.BookmarkEntity
import com.deenora.app.data.quran.JuzInfo
import com.deenora.app.data.quran.RevelationType
import com.deenora.app.data.quran.Surah
import com.deenora.app.ui.components.DeenoraTopBar
import com.deenora.app.ui.i18n.AppLanguage
import com.deenora.app.ui.i18n.AppStrings
import com.deenora.app.ui.theme.*

@Composable
fun QuranScreen(
    viewModel: QuranViewModel,
    currentLanguage: AppLanguage,
    onLanguageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    // If a surah is selected, show detail view
    state.selectedSurah?.let { surah ->
        val bookmarkedNumbers = remember(state.bookmarks, surah.number) {
            state.bookmarks.filter { it.surahNumber == surah.number }.map { it.ayahNumber }.toSet()
        }

        SurahDetailScreen(
            surah = surah,
            verses = state.currentVerses,
            fontSizeSp = state.fontSizeSp,
            isAudioPlaying = state.isAudioPlaying,
            playingAyahIndex = state.playingAyahIndex,
            bookmarkedAyahNumbers = bookmarkedNumbers,
            currentLanguage = currentLanguage,
            onBackClick = { viewModel.closeSurahDetail() },
            onToggleBookmark = { ayah -> viewModel.toggleBookmark(ayah, surah) },
            onToggleAudio = { viewModel.toggleAudio() },
            onFontSizeChange = { viewModel.updateFontSize(it) }
        )
        return
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            DeenoraTopBar(
                title = AppStrings.navQuran(currentLanguage),
                subtitle = "114 Surahs • 30 Juz",
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
            // Search Bar
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("quran_search_field"),
                placeholder = { Text(AppStrings.searchSurah(currentLanguage)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            // Tabs: Surahs, Juz, Bookmarks
            TabRow(
                selectedTabIndex = state.selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = EmeraldPrimary,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Tab(
                    selected = state.selectedTab == 0,
                    onClick = { viewModel.onTabSelected(0) },
                    text = { Text(AppStrings.surahsTab(currentLanguage), fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = state.selectedTab == 1,
                    onClick = { viewModel.onTabSelected(1) },
                    text = { Text(AppStrings.juzTab(currentLanguage), fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = state.selectedTab == 2,
                    onClick = { viewModel.onTabSelected(2) },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(AppStrings.bookmarksTab(currentLanguage), fontWeight = FontWeight.Bold)
                            if (state.bookmarks.isNotEmpty()) {
                                Badge(containerColor = GoldPrimary) {
                                    Text(state.bookmarks.size.toString())
                                }
                            }
                        }
                    }
                )
            }

            // Content according to selected tab
            when (state.selectedTab) {
                0 -> {
                    // Surahs list
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(state.filteredSurahs, key = { it.number }) { surah ->
                            SurahListItem(
                                surah = surah,
                                currentLanguage = currentLanguage,
                                onClick = { viewModel.openSurah(surah) }
                            )
                        }
                    }
                }
                1 -> {
                    // Juz list
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(state.juzList, key = { it.number }) { juz ->
                            JuzListItem(
                                juz = juz,
                                currentLanguage = currentLanguage,
                                onClick = {
                                    val surah = state.filteredSurahs.firstOrNull { it.number == juz.startSurahNumber }
                                    if (surah != null) viewModel.openSurah(surah)
                                }
                            )
                        }
                    }
                }
                2 -> {
                    // Bookmarks list
                    if (state.bookmarks.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Outlined.BookmarkBorder,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = when (currentLanguage) {
                                        AppLanguage.ARABIC -> "لا توجد آيات محفوظة بعد"
                                        AppLanguage.ENGLISH -> "No bookmarked verses yet"
                                        AppLanguage.FRENCH -> "Aucun verset enregistré pour l'instant"
                                    },
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(state.bookmarks, key = { it.id }) { bookmark ->
                                BookmarkListItem(
                                    bookmark = bookmark,
                                    currentLanguage = currentLanguage,
                                    onClick = {
                                        val surah = state.filteredSurahs.firstOrNull { it.number == bookmark.surahNumber }
                                        if (surah != null) viewModel.openSurah(surah)
                                    },
                                    onDelete = { viewModel.removeBookmark(bookmark) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SurahListItem(
    surah: Surah,
    currentLanguage: AppLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("surah_item_${surah.number}")
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Surah Number Diamond / Badge
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(EmeraldPale),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = surah.number.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldDark
                    )
                }

                Column {
                    Text(
                        text = surah.getName(currentLanguage),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${AppStrings.versesCount(currentLanguage, surah.versesCount)} • " +
                                if (surah.revelationType == RevelationType.MAKKI) AppStrings.makki(currentLanguage)
                                else AppStrings.madani(currentLanguage),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Arabic Surah Calligraphic Name
            Text(
                text = surah.nameAr,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary
            )
        }
    }
}

@Composable
fun JuzListItem(
    juz: JuzInfo,
    currentLanguage: AppLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
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
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(GoldContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = juz.number.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GoldDark
                    )
                }

                Column {
                    Text(
                        text = if (currentLanguage == AppLanguage.ARABIC) juz.nameAr else juz.nameEn,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Starts at Surah ${juz.startSurahNumber} Ayah ${juz.startAyahNumber}",
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

@Composable
fun BookmarkListItem(
    bookmark: BookmarkEntity,
    currentLanguage: AppLanguage,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.25f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Bookmark,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "${bookmark.surahNameAr} [${bookmark.surahNumber}:${bookmark.ayahNumber}]",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldDark
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = bookmark.ayahTextAr,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 26.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (bookmark.ayahTextEn.isNotEmpty() && currentLanguage != AppLanguage.ARABIC) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = bookmark.ayahTextEn,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
