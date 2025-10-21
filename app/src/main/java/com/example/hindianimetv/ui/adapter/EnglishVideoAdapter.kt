package com.example.hindianimetv.ui.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hindianimetv.R
import com.example.hindianimetv.data.model.VideoItem
import com.example.hindianimetv.databinding.ItemGridHomeBinding
import com.example.hindianimetv.util.YoutubeUtils

class EnglishVideoAdapter(private val onItemClick: (VideoItem) -> Unit) :
    ListAdapter<VideoItem, EnglishVideoAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<VideoItem>() {
            override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem) =
                oldItem.videoUrl == newItem.videoUrl

            override fun areContentsTheSame(oldItem: VideoItem, newItem: VideoItem) =
                oldItem == newItem
        }
    }

    inner class VH(val binding: ItemGridHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VideoItem, position: Int) {
            binding.tvTitle.text = item.title
            binding.tvPublisher.text = "Pub: ${item.publisher}"

            val id = YoutubeUtils.extractYoutubeId(item.videoUrl)
            val thumbUrl = id?.let { YoutubeUtils.getThumbnailUrlFromVideoId(it) }

            Glide.with(binding.root)
                .load(thumbUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .centerCrop()
                .into(binding.ivThumb)

            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGridHomeBinding.inflate(inflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position), position)
    }
}