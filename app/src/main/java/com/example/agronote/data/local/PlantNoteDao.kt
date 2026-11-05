package com.example.agronote.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.agronote.data.model.PlantNote
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantNoteDao {

    @Query("SELECT * FROM plant_notes WHERE isDeleted = 0 ORDER BY observationDate DESC")
    fun getAllNotes(): Flow<List<PlantNote>>

    @Query("SELECT * FROM plant_notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: String): PlantNote?

    @Query("SELECT * FROM plant_notes WHERE isSynced = 0")
    suspend fun getUnsyncedNotes(): List<PlantNote>

    @Query("SELECT COUNT(*) FROM plant_notes WHERE isDeleted = 0")
    fun getNotesCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: PlantNote)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notes: List<PlantNote>)

    @Update
    suspend fun updateNote(note: PlantNote)

    @Query("UPDATE plant_notes SET isDeleted = 1, isSynced = 0, updatedAt = :timestamp WHERE id = :id")
    suspend fun softDeleteNote(id: String, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM plant_notes WHERE id = :id")
    suspend fun hardDeleteNote(id: String)
}
