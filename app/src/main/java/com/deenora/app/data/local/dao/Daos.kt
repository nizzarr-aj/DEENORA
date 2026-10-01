package com.deenora.app.data.local.dao

import androidx.room.*
import com.deenora.app.data.local.entity.BookmarkEntity
import com.deenora.app.data.local.entity.CustomDhikrEntity
import com.deenora.app.data.local.entity.DhikrDailyLogEntity
import com.deenora.app.data.local.entity.TasbihRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks ORDER BY savedAtTimestamp DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE surahNumber = :surahNumber AND ayahNumber = :ayahNumber)")
    fun isBookmarked(surahNumber: Int, ayahNumber: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity): Long

    @Query("DELETE FROM bookmarks WHERE surahNumber = :surahNumber AND ayahNumber = :ayahNumber")
    suspend fun deleteBookmark(surahNumber: Int, ayahNumber: Int)

    @Delete
    suspend fun delete(bookmark: BookmarkEntity)
}

@Dao
interface TasbihDao {
    @Query("SELECT * FROM tasbih_records")
    fun getAllTasbihRecords(): Flow<List<TasbihRecordEntity>>

    @Query("SELECT * FROM tasbih_records WHERE dhikrKey = :key LIMIT 1")
    suspend fun getRecordByKey(key: String): TasbihRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(record: TasbihRecordEntity)

    @Query("UPDATE tasbih_records SET count = :count, totalLifetimeCount = totalLifetimeCount + 1, lastUpdated = :timestamp WHERE dhikrKey = :key")
    suspend fun incrementDhikr(key: String, count: Int, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE tasbih_records SET count = :count, lastUpdated = :timestamp WHERE dhikrKey = :key")
    suspend fun setCount(key: String, count: Int, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE tasbih_records SET count = 0 WHERE dhikrKey = :key")
    suspend fun resetDhikr(key: String)

    // Daily Dhikr Logs
    @Query("SELECT * FROM dhikr_daily_logs WHERE dateKey = :dateKey LIMIT 1")
    suspend fun getDailyLog(dateKey: String): DhikrDailyLogEntity?

    @Query("SELECT * FROM dhikr_daily_logs WHERE dateKey = :dateKey LIMIT 1")
    fun observeDailyLog(dateKey: String): Flow<DhikrDailyLogEntity?>

    @Query("SELECT * FROM dhikr_daily_logs ORDER BY dateKey DESC LIMIT 7")
    fun getRecentDailyLogs(): Flow<List<DhikrDailyLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDailyLog(log: DhikrDailyLogEntity)

    // Custom Dhikrs
    @Query("SELECT * FROM custom_dhikrs ORDER BY createdAt ASC")
    fun getAllCustomDhikrs(): Flow<List<CustomDhikrEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomDhikr(dhikr: CustomDhikrEntity): Long

    @Query("UPDATE custom_dhikrs SET count = :count WHERE id = :id")
    suspend fun updateCustomDhikrCount(id: Long, count: Int)

    @Delete
    suspend fun deleteCustomDhikr(dhikr: CustomDhikrEntity)
}
