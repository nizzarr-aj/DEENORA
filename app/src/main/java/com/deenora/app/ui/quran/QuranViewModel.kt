package com.deenora.app.ui.quran

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deenora.app.data.local.dao.BookmarkDao
import com.deenora.app.data.local.entity.BookmarkEntity
import com.deenora.app.data.preferences.UserPreferencesRepository
import com.deenora.app.data.quran.Ayah
import com.deenora.app.data.quran.JuzInfo
import com.deenora.app.data.quran.QuranRepository
import com.deenora.app.data.quran.Surah
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class QuranUiState(
    val searchQuery: String = "",
    val selectedTab: Int = 0,
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

    private var mediaPlayer: MediaPlayer? = null

    init {
        viewModelScope.launch {
            bookmarkDao.getAllBookmarks().collect { list ->
                _uiState.update { it.copy(bookmarks = list) }
            }
        }

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
        stopAudio()
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
        if (_uiState.value.isAudioPlaying) {
            stopAudio()
        } else {
            playAyah(_uiState.value.playingAyahIndex)
        }
    }

    private fun playAyah(index: Int) {
        val verses = _uiState.value.currentVerses
        val surah = _uiState.value.selectedSurah

        if (surah == null || index !in verses.indices) {
            _uiState.update { it.copy(isAudioPlaying = false, playingAyahIndex = 0) }
            return
        }

        releaseMediaPlayer()

        val ayah = verses[index]
        val audioUrl = buildEveryAyahUrl(surah.number, ayah.ayahNumber)

        _uiState.update {
            it.copy(
                isAudioPlaying = true,
                playingAyahIndex = index
            )
        }

        val player = MediaPlayer()
        mediaPlayer = player

        player.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )

        player.setOnPreparedListener { mp ->
            if (mediaPlayer === mp) {
                mp.start()
            } else {
                mp.release()
            }
        }

        player.setOnCompletionListener {
            val nextIndex = _uiState.value.playingAyahIndex + 1
            if (nextIndex < _uiState.value.currentVerses.size) {
                playAyah(nextIndex)
            } else {
                stopAudio()
                _uiState.update { it.copy(playingAyahIndex = 0) }
            }
        }

        player.setOnErrorListener { _, _, _ ->
            releaseMediaPlayer()
            _uiState.update { it.copy(isAudioPlaying = false) }
            true
        }

        try {
            player.setDataSource(audioUrl)
            player.prepareAsync()
        } catch (_: Exception) {
            releaseMediaPlayer()
            _uiState.update { it.copy(isAudioPlaying = false) }
        }
    }

    private fun buildEveryAyahUrl(surahNumber: Int, ayahNumber: Int): String {
        val fileName = "%03d%03d.mp3".format(surahNumber, ayahNumber)
        return "https://everyayah.com/data/Alafasy_128kbps/$fileName"
    }

    private fun stopAudio() {
        releaseMediaPlayer()
        _uiState.update { it.copy(isAudioPlaying = false) }
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.let { player ->
            try {
                if (player.isPlaying) player.stop()
            } catch (_: IllegalStateException) {
            }
            try {
                player.reset()
            } catch (_: IllegalStateException) {
            }
            player.release()
        }
        mediaPlayer = null
    }

    override fun onCleared() {
        releaseMediaPlayer()
        super.onCleared()
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
