package com.example.agronote.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.agronote.data.model.PlantNote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [PlantNote::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun plantNoteDao(): PlantNoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "agronote_database"
                )
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database.plantNoteDao())
                    }
                }
            }

            suspend fun populateInitialData(dao: PlantNoteDao) {
                // Initial sample data matching the screenshots
                val initialNotes = listOf(
                    PlantNote(
                        plantName = "Monstera Deliciosa",
                        conditionDetails = "Growing vigorously. New fenestrated leaf unfolded beautifully yesterday. Moisture levels optimal.",
                        observationDate = System.currentTimeMillis(),
                        status = "Healthy",
                        isSynced = true
                    ),
                    PlantNote(
                        plantName = "Calathea Orbifolia",
                        conditionDetails = "A few dry edges appeared on the lower leaves. Moved away from the window and increased humidity. Check again in three days.",
                        observationDate = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000L),
                        status = "Needs attention",
                        isSynced = true
                    ),
                    PlantNote(
                        plantName = "Boston Fern",
                        conditionDetails = "Recovering well from minor dehydration. Fronds are turning vibrant green after daily misting.",
                        observationDate = System.currentTimeMillis() - (6 * 24 * 60 * 60 * 1000L),
                        status = "Recovering",
                        isSynced = true
                    )
                )
                dao.insertAll(initialNotes)
            }
        }
    }
}
