package com.deenora.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.deenora.app.data.local.dao.BookmarkDao
import com.deenora.app.data.local.dao.TasbihDao
import com.deenora.app.data.local.entity.BookmarkEntity
import com.deenora.app.data.local.entity.CustomDhikrEntity
import com.deenora.app.data.local.entity.DhikrDailyLogEntity
import com.deenora.app.data.local.entity.TasbihRecordEntity

@Database(
    entities = [
        BookmarkEntity::class,
        TasbihRecordEntity::class,
        DhikrDailyLogEntity::class,
        CustomDhikrEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun tasbihDao(): TasbihDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "deenora_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
