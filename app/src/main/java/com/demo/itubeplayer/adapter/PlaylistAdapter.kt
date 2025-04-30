package com.demo.itubeplayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.itubeplayer.data.VideoEntity
import com.demo.itubeplayer.databinding.ItemVideoBinding

class PlaylistAdapter(
    private val onClick: (VideoEntity) -> Unit,
    private val onLongClick: (VideoEntity) -> Unit
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    private val items = mutableListOf<VideoEntity>()

    fun submitList(list: List<VideoEntity>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class PlaylistViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val video = items[position]
        holder.binding.tvVideoUrl.text = video.url

        holder.binding.tvVideoUrl.setOnClickListener {
            onClick(video) // ✅ pass full object
        }

        holder.binding.tvVideoUrl.setOnLongClickListener {
            onLongClick(video) // ✅ pass full object
            true
        }
    }

    override fun getItemCount(): Int = items.size
}
