package com.example.hindianimetv.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hindianimetv.R
import com.example.hindianimetv.data.model.EpisodeInfo
import com.example.hindianimetv.databinding.ItemEpisodeBinding

class EpisodeAdapter(private val onClick: (EpisodeInfo) -> Unit) :
    ListAdapter<EpisodeInfo, EpisodeAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<EpisodeInfo>() {
            override fun areItemsTheSame(oldItem: EpisodeInfo, newItem: EpisodeInfo) =
                oldItem.key == newItem.key

            override fun areContentsTheSame(oldItem: EpisodeInfo, newItem: EpisodeInfo) =
                oldItem == newItem
        }
    }

    inner class VH(private val binding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EpisodeInfo) {
            binding.tvEpisodeTitle.text = item.displayTitle
            binding.tvEpisodeCount.text = "${item.count} videos"

            // Load thumbnail if available
            Glide.with(binding.root)
                .load(item.thumbUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.ivEpisodeThumb)

            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }
}
