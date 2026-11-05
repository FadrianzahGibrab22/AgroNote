package com.example.agronote

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.agronote.data.local.AppDatabase
import com.example.agronote.data.repository.PlantNoteRepository
import com.example.agronote.data.sync.SyncWorker
import java.util.concurrent.TimeUnit

class AgroNoteApp : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { PlantNoteRepository(database.plantNoteDao(), this) }

    override fun onCreate() {
        super.onCreate()
        setupBackgroundSync()
    }

    private fun setupBackgroundSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "AgroNoteSyncWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            syncWorkRequest
        )
    }
}
