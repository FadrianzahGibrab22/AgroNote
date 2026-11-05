package com.example.agronote.ui.add

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.agronote.AgroNoteApp
import com.example.agronote.R
import com.example.agronote.data.model.PlantNote
import com.example.agronote.databinding.ActivityAddNoteBinding
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private var selectedDateTimestamp: Long = System.currentTimeMillis()
    private var localPhotoPath: String? = null
    private var currentPhotoUri: Uri? = null
    private var editingNoteId: String? = null

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                currentPhotoUri?.let { uri ->
                    saveUriToInternalStorage(uri)
                }
            }
        }

    private val selectPhotoLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                saveUriToInternalStorage(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editingNoteId = intent.getStringExtra(EXTRA_EDIT_NOTE_ID)

        setupViews()
        setupListeners()

        if (editingNoteId != null) {
            loadNoteData(editingNoteId!!)
        } else {
            // Memastikan kotak foto bersih untuk entri baru
            localPhotoPath = null
            binding.imgPreviewPhoto.setImageDrawable(null)
            binding.imgPreviewPhoto.visibility = android.view.View.GONE
            binding.layoutPhotoPlaceholder.visibility = android.view.View.VISIBLE
        }
    }

    private fun setupViews() {
        updateDateDisplay(selectedDateTimestamp)
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnSelectDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnCamera.setOnClickListener {
            dispatchTakePicture()
        }

        binding.btnGallery.setOnClickListener {
            selectPhotoLauncher.launch("image/*")
        }

        binding.btnSaveLog.setOnClickListener {
            saveNote()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance().apply { timeInMillis = selectedDateTimestamp }
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val newCalendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }
                selectedDateTimestamp = newCalendar.timeInMillis
                updateDateDisplay(selectedDateTimestamp)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun updateDateDisplay(timestamp: Long) {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        binding.tvSelectedDate.text = dateFormat.format(Date(timestamp))
    }

    private fun dispatchTakePicture() {
        val photoFile = File(cacheDir, "temp_plant_${java.util.UUID.randomUUID()}.jpg")
        val uri = androidx.core.content.FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.fileprovider",
            photoFile
        )
        currentPhotoUri = uri
        takePictureLauncher.launch(uri)
    }

    private fun saveUriToInternalStorage(sourceUri: Uri) {
        try {
            val photosDir = File(filesDir, "plant_photos").apply { if (!exists()) mkdirs() }

            // Menggunakan UUID unik agar setiap foto memiliki nama file sendiri dan tidak saling menimpa
            val uniqueFileName = "plant_${java.util.UUID.randomUUID()}.jpg"
            val destFile = File(photosDir, uniqueFileName)

            contentResolver.openInputStream(sourceUri)?.use { input ->
                java.io.FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }

            // Simpan path absolut unik ke variabel
            localPhotoPath = destFile.absolutePath
            showPhotoPreview(localPhotoPath!!)
        } catch (e: Exception) {
            android.widget.Toast.makeText(this, "Failed to save photo: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPhotoPreview(path: String) {
        binding.layoutPhotoPlaceholder.visibility = View.GONE
        binding.imgPreviewPhoto.visibility = View.VISIBLE

        // Menambahkan perintah skipMemoryCache agar Glide selalu memuat foto TERBARU
        com.bumptech.glide.Glide.with(this)
            .load(java.io.File(path))
            .skipMemoryCache(true)
            .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE)
            .into(binding.imgPreviewPhoto)
    }

    private fun getSelectedStatus(): String {
        return when (binding.chipGroupStatus.checkedChipId) {
            R.id.chipAttention -> "Needs attention"
            R.id.chipRecovering -> "Recovering"
            else -> "Healthy"
        }
    }

    private fun loadNoteData(noteId: String) {
        binding.tvHeaderLabel.text = "EDIT ENTRY"
        binding.tvHeaderTitle.text = "Edit plant note"

        lifecycleScope.launch {
            val repository = (application as AgroNoteApp).repository
            val note = repository.getNoteById(noteId)
            note?.let {
                binding.etPlantName.setText(it.plantName)
                binding.etConditionDetails.setText(it.conditionDetails)
                selectedDateTimestamp = it.observationDate
                updateDateDisplay(selectedDateTimestamp)

                when (it.status) {
                    "Needs attention" -> binding.chipAttention.isChecked = true
                    "Recovering" -> binding.chipRecovering.isChecked = true
                    else -> binding.chipHealthy.isChecked = true
                }

                if (!it.localPhotoPath.isNullOrEmpty() && File(it.localPhotoPath).exists()) {
                    localPhotoPath = it.localPhotoPath
                    showPhotoPreview(localPhotoPath!!)
                }
            }
        }
    }

    private fun saveNote() {
        val plantName = binding.etPlantName.text.toString().trim()
        val condition = binding.etConditionDetails.text.toString().trim()
        val status = getSelectedStatus()

        if (plantName.isEmpty()) {
            binding.etPlantName.error = "Plant name is required"
            binding.etPlantName.requestFocus()
            return
        }

        lifecycleScope.launch {
            val repository = (application as AgroNoteApp).repository
            if (editingNoteId != null) {
                val updatedNote = PlantNote(
                    id = editingNoteId!!,
                    plantName = plantName,
                    conditionDetails = condition,
                    observationDate = selectedDateTimestamp,
                    status = status,
                    localPhotoPath = localPhotoPath,
                    isSynced = false
                )
                repository.updateNote(updatedNote)

                // TAMBAHKAN BARIS INI: Langsung tembak data hasil edit ke Firebase!
                repository.syncPendingNotes()

                Toast.makeText(this@AddNoteActivity, "Note updated!", Toast.LENGTH_SHORT).show()
            }

            else {
                val newNote = PlantNote(
                    plantName = plantName,
                    conditionDetails = condition,
                    observationDate = selectedDateTimestamp,
                    status = status,
                    localPhotoPath = localPhotoPath,
                    isSynced = false
                )
                repository.insertNote(newNote)

                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                    repository.syncPendingNotes()
                }
                
                Toast.makeText(this@AddNoteActivity, "Plant note saved offline!", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_EDIT_NOTE_ID = "extra_edit_note_id"
    }
}
