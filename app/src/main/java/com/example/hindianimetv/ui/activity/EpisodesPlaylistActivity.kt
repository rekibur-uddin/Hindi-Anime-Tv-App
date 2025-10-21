package com.example.hindianimetv.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hindianimetv.data.network.RetrofitClient
import com.example.hindianimetv.databinding.ActivityEpisodesPlaylistBinding
import com.example.hindianimetv.util.YoutubeUtils
import kotlinx.coroutines.launch
import android.widget.Toast
import com.example.hindianimetv.ui.adapter.EnglishVideoAdapter
import java.util.Locale

class EpisodesPlaylistActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_EPISODE_KEY = "extra_episode_key"
        fun start(context: Context, episodeKey: String) {
            val i = Intent(context, EpisodesPlaylistActivity::class.java)
            i.putExtra(EXTRA_EPISODE_KEY, episodeKey)
            context.startActivity(i)
        }
    }

    private lateinit var binding: ActivityEpisodesPlaylistBinding
    private val videoAdapter by lazy { EnglishVideoAdapter { openPlayer(it) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEpisodesPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvEpisodePlaylist.layoutManager = LinearLayoutManager(this)
        binding.rvEpisodePlaylist.adapter = videoAdapter

        val episodeKey = intent.getStringExtra(EXTRA_EPISODE_KEY)
        binding.toolBar.title = episodeKey?.replace('_', ' ')?.capitalize(Locale.ROOT) ?: "Episode"

        if (episodeKey == null) {
            Toast.makeText(this, "Episode not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Toolbar
        setSupportActionBar(binding.toolBar)
        binding.toolBar.setNavigationOnClickListener { finish() }

        // Fetch playlists and show the selected episode list
        lifecycleScope.launch {
            try {
                val map = RetrofitClient.apiService.getPlaylists()
                val list = map[episodeKey] ?: emptyList()
                videoAdapter.submitList(list)
            } catch (e: Exception) {
                Toast.makeText(this@EpisodesPlaylistActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openPlayer(item: com.example.hindianimetv.data.model.VideoItem) {
        val videoId = YoutubeUtils.extractYoutubeId(item.videoUrl)
        val episodeKey = intent.getStringExtra(EXTRA_EPISODE_KEY)
        if (videoId != null && episodeKey != null) {
            val intent = Intent(this, VideoPlayerActivity::class.java)
            intent.putExtra(VideoPlayerActivity.EXTRA_VIDEO_ID, videoId)
            intent.putExtra(VideoPlayerActivity.EXTRA_TITLE, item.title)
            intent.putExtra(VideoPlayerActivity.EXTRA_PUBLISHER, item.publisher)
            intent.putExtra(VideoPlayerActivity.EXTRA_PLAYLIST_KEY, episodeKey)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Invalid video id or playlist", Toast.LENGTH_SHORT).show()
        }
    }
}
