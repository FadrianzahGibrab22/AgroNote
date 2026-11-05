package com.example.agronote.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agronote.AgroNoteApp
import com.example.agronote.R
import com.example.agronote.databinding.ActivityMainBinding
import com.example.agronote.ui.adapter.PlantNoteAdapter
import com.example.agronote.ui.add.AddNoteActivity
import com.example.agronote.ui.detail.DetailNoteActivity
import com.example.agronote.utils.NetworkUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PlantNoteAdapter
    private lateinit var networkUtils: NetworkUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupRecyclerView()
        observeData()
        observeNetwork()
    }

    private fun setupUI() {
        val dateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        binding.tvTodayDate.text = dateFormat.format(Date()).uppercase()

        binding.tvGreeting.text = "Good morning, Maya"

        binding.fabAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        binding.tvViewAll.setOnClickListener {
            // Future filter or full page
        }
    }

    private fun setupRecyclerView() {
        adapter = PlantNoteAdapter { note ->
            val intent = Intent(this, DetailNoteActivity::class.java).apply {
                putExtra(DetailNoteActivity.EXTRA_NOTE_ID, note.id)
            }
            startActivity(intent)
        }

        binding.rvPlantNotes.layoutManager = LinearLayoutManager(this)
        binding.rvPlantNotes.adapter = adapter
    }

    private fun observeData() {
        val repository = (application as AgroNoteApp).repository

        lifecycleScope.launch {
            repository.allNotes.collectLatest { notes ->
                adapter.submitList(notes.toList()) // .toList() memaksa daftar membuat salinan baru
                adapter.notifyDataSetChanged()      // Memaksa seluruh foto langsung ter-update seketika
                binding.tvEmptyState.visibility = if (notes.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            repository.notesCount.collectLatest { count ->
                binding.tvNotesCount.text = "$count plant notes in your garden"
            }
        }
    }

    private fun observeNetwork() {
        networkUtils = NetworkUtils(this)
        networkUtils.observe(this) { isOnline ->
            if (isOnline) {
                binding.tvNetworkStatus.text = "● Online"
                binding.tvNetworkStatus.setBackgroundResource(R.drawable.bg_badge_online)
                binding.tvNetworkStatus.setTextColor(
                    ContextCompat.getColor(this, R.color.badge_online_text)
                )
            } else {
                binding.tvNetworkStatus.text = "● Offline"
                binding.tvNetworkStatus.setBackgroundResource(R.drawable.bg_badge_offline)
                binding.tvNetworkStatus.setTextColor(
                    ContextCompat.getColor(this, R.color.badge_offline_text)
                )
            }
        }
    }
}
