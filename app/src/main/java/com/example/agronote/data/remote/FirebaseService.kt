package com.example.agronote.data.remote

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.agronote.data.model.PlantNote
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream
import java.io.File

class FirebaseService(private val context: Context) {

    private val firestore by lazy { FirebaseFirestore.getInstance() }

    suspend fun syncNoteToCloud(note: PlantNote): Boolean {
        return try {
            var photoBase64: String? = null

            // Konversi foto menjadi Base64 terkompresi agar hemat kuota dan masuk ke Firestore
            if (!note.localPhotoPath.isNullOrEmpty() && File(note.localPhotoPath).exists()) {
                val bitmap = BitmapFactory.decodeFile(note.localPhotoPath)
                if (bitmap != null) {
                    val outputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
                    val byteArray = outputStream.toByteArray()
                    photoBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
                }
            }

            // Data yang dikirim ke server Firestore
            val noteMap = hashMapOf(
                "id" to note.id,
                "plantName" to note.plantName,
                "conditionDetails" to note.conditionDetails,
                "observationDate" to note.observationDate,
                "status" to note.status,
                "photoBase64" to photoBase64,
                "updatedAt" to System.currentTimeMillis()
            )

            // Simpan ke koleksi 'plant_notes' di Cloud Firestore
            val task = firestore.collection("plant_notes")
                .document(note.id)
                .set(noteMap)

            Tasks.await(task)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteNoteFromCloud(noteId: String): Boolean {
        return try {
            val task = firestore.collection("plant_notes")
                .document(noteId)
                .delete()
            Tasks.await(task)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}