package com.example.agronote.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.UUID

@Entity(tableName = "plant_notes")
data class PlantNote(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val plantName: String,
    val conditionDetails: String,
    val observationDate: Long,
    val status: String, // "Healthy", "Needs attention", "Recovering"
    val localPhotoPath: String? = null,
    val cloudPhotoUrl: String? = null,
    val isSynced: Boolean = false,
    val isDeleted: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis(),
    val userId: String = "guest_user"
) : Serializable
