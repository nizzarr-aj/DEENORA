package com.deenora.app.ui.quran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deenora.app.data.local.dao.BookmarkDao
import com.deenora.app.data.local.entity.BookmarkEntity
import com.deenora.app.data.preferences.UserPreferencesRepository
import com.deenora.app.data.quran.Ayah
import com.deenora.app.data.quran.JuzInfo
import com.deenora.app.data.quran.QuranRepository
import com.deenora.app.data.quran.Surah
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class QuranUiState(
    val searchQuery: String = "",
    val selectedTab: Int = 0, // 0 = Surahs, 1 = Juz, 2 = Bookmarks
    val filteredSurahs: List<Surah> = QuranRepository.SURAHS,
    val juzList: List<JuzInfo> = QuranRepository.JUZ_LIST,
    val bookmarks: List<BookmarkEntity> = emptyList(),
    val selectedSurah: Surah? = null,
    val currentVerses: List<Ayah> = emptyList(),
    val fontSizeSp: Float = 22f,
    val isAudioPlaying: Boolean = false,
    val playingAyahIndex: Int = 0
)

class QuranViewModel(
    private val bookmarkDao: BookmarkDao,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuranUiState())
    val uiState: StateFlow<QuranUiState> = _uiState.asStateFlow()

    private var audioJob: Job? = null

    init {
        // Collect bookmarks from Room
        viewModelScope.launch {
            bookmarkDao.getAllBookmarks().collect { list ->
                _uiState.update { it.copy(bookmarks = list) }
            }
        }

        // Collect font size preference
        viewModelScope.launch {
            preferencesRepository.settings.collect { settings ->
                _uiState.update { it.copy(fontSizeSp = settings.quranFontSize) }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        val trimmed = query.trim()
        val filtered = if (trimmed.isEmpty()) {
            QuranRepository.SURAHS
        } else {
            QuranRepository.SURAHS.filter {
                it.nameAr.contains(trimmed, ignoreCase = true) ||
                it.nameEn.contains(trimmed, ignoreCase = true) ||
                it.nameFr.contains(trimmed, ignoreCase = true) ||
                it.number.toString() == trimmed
            }
        }
        _uiState.update { it.copy(searchQuery = query, filteredSurahs = filtered) }
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    fun openSurah(surah: Surah) {
        val verses = QuranRepository.getVersesForSurah(surah.number)
        _uiState.update {
            it.copy(
                selectedSurah = surah,
                currentVerses = verses,
                isAudioPlaying = false,
                playingAyahIndex = 0
            )
        }
    }

    fun closeSurahDetail() {
        stopAudio()
        _uiState.update { it.copy(selectedSurah = null, currentVerses = emptyList()) }
    }

    fun toggleAudio() {
        val currentlyPlaying = _uiState.value.isAudioPlaying
        if (currentlyPlaying) {
            stopAudio()
        } else {
            startAudioSimulation()
        }
    }

    private fun startAudioSimulation() {
        _uiState.update { it.copy(isAudioPlaying = true) }
        audioJob?.cancel()
        audioJob = viewModelScope.launch {
            val totalVerses = _uiState.value.currentVerses.size
            while (_uiState.value.isAudioPlaying) {
                delay(4000L) // 4 seconds per ayah preview
                val nextIdx = _uiState.value.playingAyahIndex + 1
                if (nextIdx < totalVerses) {
                    _uiState.update { it.copy(playingAyahIndex = nextIdx) }
                } else {
                    _uiState.update { it.copy(isAudioPlaying = false, playingAyahIndex = 0) }
                    break
                }
            }
        }
    }

    private fun stopAudio() {
        audioJob?.cancel()
        audioJob = null
        _uiState.update { it.copy(isAudioPlaying = false) }
    }

    fun toggleBookmark(ayah: Ayah, surah: Surah) {
        viewModelScope.launch {
            val isBookmarked = _uiState.value.bookmarks.any {
                it.surahNumber == surah.number && it.ayahNumber == ayah.ayahNumber
            }
            if (isBookmarked) {
                bookmarkDao.deleteBookmark(surah.number, ayah.ayahNumber)
            } else {
                bookmarkDao.insertBookmark(
                    BookmarkEntity(
                        surahNumber = surah.number,
                        surahNameAr = surah.nameAr,
                        surahNameEn = surah.nameEn,
                        ayahNumber = ayah.ayahNumber,
                        ayahTextAr = ayah.textAr,
                        ayahTextEn = ayah.textEn
                    )
                )
            }
        }
    }

    fun removeBookmark(bookmark: BookmarkEntity) {
        viewModelScope.launch {
            bookmarkDao.delete(bookmark)
        }
    }

    fun updateFontSize(newSize: Float) {
        preferencesRepository.updateQuranFontSize(newSize)
    }
}
