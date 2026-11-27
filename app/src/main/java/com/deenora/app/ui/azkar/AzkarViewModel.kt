package com.deenora.app.ui.azkar

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deenora.app.data.azkar.AzkarRepository
import com.deenora.app.data.azkar.ZikrCategory
import com.deenora.app.data.azkar.ZikrItem
import com.deenora.app.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AzkarUiState(
    val selectedCategory: ZikrCategory = ZikrCategory.MORNING,
    val items: List<ZikrItem> = emptyList(),
    val counts: Map<String, Int> = emptyMap(),
    val completedItems: Set<String> = emptySet()
)

class AzkarViewModel(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AzkarUiState())
    val uiState: StateFlow<AzkarUiState> = _uiState.asStateFlow()

    init {
        loadCategory(ZikrCategory.MORNING)
    }

    fun selectCategory(category: ZikrCategory) {
        loadCategory(category)
    }

    private fun loadCategory(category: ZikrCategory) {
        val categoryItems = AzkarRepository.AZKAR_ITEMS.filter { it.category == category }
        _uiState.update {
            it.copy(
                selectedCategory = category,
                items = categoryItems
            )
        }
    }

    fun incrementCount(item: ZikrItem, context: Context) {
        val current = _uiState.value.counts[item.id] ?: 0
        if (current < item.targetCount) {
            val next = current + 1
            val updatedCounts = _uiState.value.counts.toMutableMap().apply {
                put(item.id, next)
            }
            val updatedCompleted = _uiState.value.completedItems.toMutableSet().apply {
                if (next >= item.targetCount) add(item.id)
            }

            _uiState.update {
                it.copy(counts = updatedCounts, completedItems = updatedCompleted)
            }

            // Haptic vibration
            if (preferencesRepository.settings.value.isVibrationEnabled) {
                performHapticPulse(context, isComplete = next >= item.targetCount)
            }
        }
    }

    fun resetItem(item: ZikrItem) {
        val updatedCounts = _uiState.value.counts.toMutableMap().apply {
            put(item.id, 0)
        }
        val updatedCompleted = _uiState.value.completedItems.toMutableSet().apply {
            remove(item.id)
        }
        _uiState.update {
            it.copy(counts = updatedCounts, completedItems = updatedCompleted)
        }
    }

    fun resetAllCurrentCategory() {
        val currentIds = _uiState.value.items.map { it.id }.toSet()
        val updatedCounts = _uiState.value.counts.toMutableMap().apply {
            currentIds.forEach { remove(it) }
        }
        val updatedCompleted = _uiState.value.completedItems.toMutableSet().apply {
            removeAll(currentIds)
        }
        _uiState.update {
            it.copy(counts = updatedCounts, completedItems = updatedCompleted)
        }
    }

    private fun performHapticPulse(context: Context, isComplete: Boolean) {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
            if (vibrator?.hasVibrator() == true) {
                if (isComplete) {
                    vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 80, 50, 100), -1))
                } else {
                    vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
                }
            }
        } catch (_: Exception) {}
    }
}
