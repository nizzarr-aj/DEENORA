package com.deenora.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val surahNumber: Int,
    val surahNameAr: String,
    val surahNameEn: String,
    val ayahNumber: Int,
    val ayahTextAr: String,
    val ayahTextEn: String,
    val savedAtTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "tasbih_records")
data class TasbihRecordEntity(
    @PrimaryKey
    val dhikrKey: String,
    val dhikrArabic: String,
    val dhikrTransliteration: String,
    val count: Int,
    val target: Int,
    val totalLifetimeCount: Long = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "dhikr_daily_logs")
data class DhikrDailyLogEntity(
    @PrimaryKey
    val dateKey: String, // "yyyy-MM-dd" e.g. "2026-10-01"
    val totalCount: Int,
    val dailyGoal: Int,
    val completedSets: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "custom_dhikrs")
data class CustomDhikrEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val arabic: String,
    val transliteration: String,
    val translation: String,
    val target: Int = 33,
    val count: Int = 0,
    val lifetimeCount: Long = 0,
    val createdAt: Long = System.currentTimeMillis()
)
