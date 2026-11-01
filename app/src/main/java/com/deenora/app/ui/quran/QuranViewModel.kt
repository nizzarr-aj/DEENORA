package com.deenora.app.ui.quran

import android.content.Context
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

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
    private val appContext: Context,
    private val bookmarkDao: BookmarkDao,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuranUiState())
    val uiState: StateFlow<QuranUiState> = _uiState.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null
    private var audioRequestJob: Job? = null
    @Volatile private var alOssiServer: String? = null

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
        val filtered = if (trimmed.isEmpty()) QuranRepository.SURAHS else QuranRepository.SURAHS.filter {
            it.nameAr.contains(trimmed, ignoreCase = true) ||
                it.nameEn.contains(trimmed, ignoreCase = true) ||
                it.nameFr.contains(trimmed, ignoreCase = true) ||
                it.number.toString() == trimmed
        }
        _uiState.update { it.copy(searchQuery = query, filteredSurahs = filtered) }
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    fun openSurah(surah: Surah) {
        stopAudio()
        val verses = QuranRepository.getVersesForSurah(appContext, surah.number)
        _uiState.update {
            it.copy(selectedSurah = surah, currentVerses = verses, isAudioPlaying = false, playingAyahIndex = 0)
        }
    }

    fun closeSurahDetail() {
        stopAudio()
        _uiState.update { it.copy(selectedSurah = null, currentVerses = emptyList()) }
    }

    fun toggleAudio() {
        if (_uiState.value.isAudioPlaying || audioRequestJob?.isActive == true) {
            stopAudio()
        } else {
            playSurah()
        }
    }

    private fun playSurah() {
        val surah = _uiState.value.selectedSurah ?: return
        stopAudio()
        _uiState.update { it.copy(isAudioPlaying = true, playingAyahIndex = -1) }

        audioRequestJob = viewModelScope.launch {
            val audioUrl = try {
                withContext(Dispatchers.IO) { resolveAlOssiSurahUrl(surah.number) }
            } catch (_: Exception) {
                null
            }

            if (!isActive) return@launch
            if (audioUrl.isNullOrBlank()) {
                _uiState.update { it.copy(isAudioPlaying = false, playingAyahIndex = 0) }
                return@launch
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
                if (mediaPlayer === mp) mp.start() else mp.release()
            }
            player.setOnCompletionListener {
                if (mediaPlayer === it) {
                    releaseMediaPlayer()
                    _uiState.update { state -> state.copy(isAudioPlaying = false, playingAyahIndex = 0) }
                }
            }
            player.setOnErrorListener { mp, _, _ ->
                if (mediaPlayer === mp) {
                    releaseMediaPlayer()
                    _uiState.update { state -> state.copy(isAudioPlaying = false, playingAyahIndex = 0) }
                }
                true
            }

            try {
                player.setDataSource(audioUrl)
                player.prepareAsync()
            } catch (_: Exception) {
                releaseMediaPlayer()
                _uiState.update { it.copy(isAudioPlaying = false, playingAyahIndex = 0) }
            }
        }
    }

    private fun resolveAlOssiSurahUrl(surahNumber: Int): String? {
        var server = alOssiServer
        if (server.isNullOrBlank()) {
            val connection = (URL("https://mp3quran.net/api/v3/reciters?language=ar").openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 12000
                readTimeout = 15000
                setRequestProperty("Accept", "application/json")
                setRequestProperty("User-Agent", "Deenk-Android")
            }
            try {
                val json = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                val reciters = JSONObject(json).optJSONArray("reciters") ?: return null
                for (i in 0 until reciters.length()) {
                    val reciter = reciters.optJSONObject(i) ?: continue
                    val name = reciter.optString("name")
                    if (!name.contains("العوسي") && !name.contains("Al-Ossi", true) && !name.contains("Aloosi", true)) continue
                    val moshaf = reciter.optJSONArray("moshaf") ?: continue
                    for (j in 0 until moshaf.length()) {
                        val item = moshaf.optJSONObject(j) ?: continue
                        val candidate = item.optString("server")
                        val surahList = item.optString("surah_list")
                        if (candidate.isNotBlank() && (surahList.isBlank() || surahList.split(",").any { it.trim() == surahNumber.toString() })) {
                            server = if (candidate.endsWith("/")) candidate else "$candidate/"
                            break
                        }
                    }
                    if (!server.isNullOrBlank()) break
                }
            } finally {
                connection.disconnect()
            }
        }
        if (server.isNullOrBlank()) return null
        alOssiServer = server
        return server + "%03d.mp3".format(Locale.US, surahNumber)
    }

    private fun stopAudio() {
        audioRequestJob?.cancel()
        audioRequestJob = null
        releaseMediaPlayer()
        _uiState.update { it.copy(isAudioPlaying = false, playingAyahIndex = 0) }
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.let { player ->
            try { if (player.isPlaying) player.stop() } catch (_: IllegalStateException) { }
            try { player.reset() } catch (_: IllegalStateException) { }
            player.release()
        }
        mediaPlayer = null
    }

    override fun onCleared() {
        audioRequestJob?.cancel()
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

