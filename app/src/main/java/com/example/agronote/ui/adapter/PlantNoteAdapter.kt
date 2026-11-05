package com.example.agronote.ui.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agronote.R
import com.example.agronote.data.model.PlantNote
import com.example.agronote.databinding.ItemPlantNoteBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlantNoteAdapter(
    private val onItemClick: (PlantNote) -> Unit
) : ListAdapter<PlantNote, PlantNoteAdapter.PlantViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val binding = ItemPlantNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlantViewHolder(private val binding: ItemPlantNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: PlantNote) {
            val context = binding.root.context

            binding.tvPlantName.text = note.plantName
            
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            binding.tvDate.text = dateFormat.format(Date(note.observationDate))

            // Status Badge Colors
            binding.tvStatusBadge.text = "● ${note.status}"
            when (note.status) {
                "Healthy" -> {
                    binding.tvStatusBadge.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.badge_healthy_bg))
                    binding.tvStatusBadge.setTextColor(
                        ContextCompat.getColor(context, R.color.badge_healthy_text)
                    )
                }
                "Needs attention" -> {
                    binding.tvStatusBadge.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.badge_attention_bg))
                    binding.tvStatusBadge.setTextColor(
                        ContextCompat.getColor(context, R.color.badge_attention_text)
                    )
                }
                else -> {
                    binding.tvStatusBadge.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.badge_recovering_bg))
                    binding.tvStatusBadge.setTextColor(
                        ContextCompat.getColor(context, R.color.badge_recovering_text)
                    )
                }
            }

            // Thumbnail Image
            if (!note.localPhotoPath.isNullOrEmpty() && File(note.localPhotoPath).exists()) {
                // WAJIB: Netralkan pewarna hijau agar foto asli tampil jernih!
                binding.imgPlantThumbnail.imageTintList = null

                Glide.with(context)
                    .load(File(note.localPhotoPath))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE)
                    .placeholder(R.color.bg_light_sage)
                    .into(binding.imgPlantThumbnail)
            } else if (!note.cloudPhotoUrl.isNullOrEmpty()) {
                // WAJIB: Netralkan pewarna hijau
                binding.imgPlantThumbnail.imageTintList = null

                Glide.with(context)
                    .load(note.cloudPhotoUrl)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE)
                    .placeholder(R.color.bg_light_sage)
                    .into(binding.imgPlantThumbnail)
            } else {
                // Hanya pasang warna hijau jika memang TANPA FOTO (ikon daun)
                Glide.with(context).clear(binding.imgPlantThumbnail)
                binding.imgPlantThumbnail.setImageResource(R.drawable.ic_leaf_logo)
                binding.imgPlantThumbnail.imageTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary_green))
            }

            binding.root.setOnClickListener {
                onItemClick(note)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<PlantNote>() {
        override fun areItemsTheSame(oldItem: PlantNote, newItem: PlantNote): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PlantNote, newItem: PlantNote): Boolean {
            // Memeriksa secara detail path foto dan tanggal update agar RecyclerView langsung refresh seketika
            return oldItem.plantName == newItem.plantName &&
                    oldItem.status == newItem.status &&
                    oldItem.conditionDetails == newItem.conditionDetails &&
                    oldItem.localPhotoPath == newItem.localPhotoPath
        }
    }
}
