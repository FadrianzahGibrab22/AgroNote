package com.example.agronote.ui.detail

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.agronote.AgroNoteApp
import com.example.agronote.R
import com.example.agronote.data.model.PlantNote
import com.example.agronote.databinding.ActivityDetailNoteBinding
import com.example.agronote.ui.add.AddNoteActivity
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailNoteBinding
    private var currentNoteId: String? = null
    private var currentNote: PlantNote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentNoteId = intent.getStringExtra(EXTRA_NOTE_ID)

        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        currentNoteId?.let { loadNote(it) }
    }

    private fun setupListeners() {
        binding.btnBackDetail.setOnClickListener {
            finish()
        }

        binding.btnDeleteNote.setOnClickListener {
            showDeleteConfirmation()
        }

        binding.btnEditNote.setOnClickListener {
            currentNoteId?.let { id ->
                val intent = Intent(this, AddNoteActivity::class.java).apply {
                    putExtra(AddNoteActivity.EXTRA_EDIT_NOTE_ID, id)
                }
                startActivity(intent)
            }
        }
    }

    private fun loadNote(id: String) {
        lifecycleScope.launch {
            val repository = (application as AgroNoteApp).repository
            val note = repository.getNoteById(id)
            if (note == null) {
                finish()
                return@launch
            }
            currentNote = note

            binding.tvDetailPlantName.text = note.plantName
            binding.tvDetailCondition.text = if (note.conditionDetails.isNotEmpty()) {
                note.conditionDetails
            } else {
                "No additional notes provided."
            }

            val dateFormat = java.text.SimpleDateFormat("MMMM d, yyyy", java.util.Locale.ENGLISH)
            binding.tvDetailDate.text = "Observed on ${dateFormat.format(java.util.Date(note.observationDate))}"

            // Status Badge
            binding.tvDetailStatusBadge.text = "● ${note.status}"
            when (note.status) {
                "Healthy" -> {
                    binding.tvDetailStatusBadge.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(this@DetailNoteActivity, R.color.badge_healthy_bg))
                    binding.tvDetailStatusBadge.setTextColor(
                        ContextCompat.getColor(this@DetailNoteActivity, R.color.badge_healthy_text)
                    )
                }
                "Needs attention" -> {
                    binding.tvDetailStatusBadge.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(this@DetailNoteActivity, R.color.badge_attention_bg))
                    binding.tvDetailStatusBadge.setTextColor(
                        ContextCompat.getColor(this@DetailNoteActivity, R.color.badge_attention_text)
                    )
                }
                else -> {
                    binding.tvDetailStatusBadge.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(this@DetailNoteActivity, R.color.badge_recovering_bg))
                    binding.tvDetailStatusBadge.setTextColor(
                        ContextCompat.getColor(this@DetailNoteActivity, R.color.badge_recovering_text)
                    )
                }
            }

            // Hero Image
            if (!note.localPhotoPath.isNullOrEmpty() && File(note.localPhotoPath).exists()) {
                Glide.with(this@DetailNoteActivity)
                    .load(File(note.localPhotoPath))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE)
                    .into(binding.imgHeroPhoto)
            } else {
                binding.imgHeroPhoto.setImageResource(R.drawable.bg_header_curved)
                binding.imgHeroPhoto.imageTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this@DetailNoteActivity, R.color.primary_dark_green))
            }
        }
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Delete Plant Note")
            .setMessage("Are you sure you want to delete this plant note? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                currentNoteId?.let { id ->
                    lifecycleScope.launch {
                        val repository = (application as AgroNoteApp).repository
                        repository.deleteNote(id)
                        Toast.makeText(this@DetailNoteActivity, "Note deleted", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    companion object {
        const val EXTRA_NOTE_ID = "extra_note_id"
    }
}
