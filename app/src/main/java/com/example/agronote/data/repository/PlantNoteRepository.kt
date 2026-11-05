package com.example.agronote.data.repository

import android.content.Context
import com.example.agronote.data.local.PlantNoteDao
import com.example.agronote.data.model.PlantNote
import com.example.agronote.data.remote.FirebaseService
import kotlinx.coroutines.flow.Flow

class PlantNoteRepository(
    private val dao: PlantNoteDao,
    context: Context
) {
    private val firebaseService = FirebaseService(context)

    val allNotes: Flow<List<PlantNote>> = dao.getAllNotes()
    val notesCount: Flow<Int> = dao.getNotesCount()

    suspend fun getNoteById(id: String): PlantNote? {
        return dao.getNoteById(id)
    }

    suspend fun insertNote(note: PlantNote) {
        dao.insertNote(note.copy(isSynced = false, updatedAt = System.currentTimeMillis()))
    }

    suspend fun updateNote(note: PlantNote) {
        dao.updateNote(note.copy(isSynced = false, updatedAt = System.currentTimeMillis()))
    }

    suspend fun deleteNote(id: String) {
        // 1. Hapus dari database lokal (Room)
        dao.hardDeleteNote(id)

        // 2. Langsung hapus dari server Cloud Firestore!
        firebaseService.deleteNoteFromCloud(id)
    }

    suspend fun syncPendingNotes(): Boolean {
        val unsynced = dao.getUnsyncedNotes()
        var allSuccess = true
        for (note in unsynced) {
            if (note.isDeleted) {
                // If soft-deleted, delete permanently after cloud acknowledges
                dao.hardDeleteNote(note.id)
            } else {
                val success = firebaseService.syncNoteToCloud(note)
                if (success) {
                    dao.updateNote(note.copy(isSynced = true))
                } else {
                    allSuccess = false
                }
            }
        }
        return allSuccess
    }
}
