package com.example.hindianimetv.ui.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.hindianimetv.R
import com.example.hindianimetv.data.network.RetrofitClient
import com.example.hindianimetv.databinding.ActivityVideoPlayerBinding
import com.example.hindianimetv.ui.adapter.EnglishVideoAdapter
import com.example.hindianimetv.util.YoutubeUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.coroutines.launch
import android.widget.Toast

class VideoPlayerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_VIDEO_ID = "extra_video_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_PUBLISHER = "extra_publisher"
        const val EXTRA_PLAYLIST_KEY = "extra_playlist_key"
    }

    private lateinit var binding: ActivityVideoPlayerBinding
    private val videoAdapter by lazy { EnglishVideoAdapter { playVideo(it) } }
    private var youTubePlayer: YouTubePlayer? = null
    private var isFullScreen = false
    private var currentPlaybackPosition = 0f

    private val pub = "Upload By:"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize YouTubePlayerView
        lifecycle.addObserver(binding.youtubePlayerView)
        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@VideoPlayerActivity.youTubePlayer = youTubePlayer
                val videoId = intent.getStringExtra(EXTRA_VIDEO_ID)
                if (videoId != null) {
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                currentPlaybackPosition = second
            }
        })

        // Setup title and publisher
        val title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        val publisher = intent.getStringExtra(EXTRA_PUBLISHER) ?: ""
        binding.vidTitle.text = title
        binding.vidPublisher.text = "$pub$publisher"

        // Setup rvOtherVid
        binding.rvOtherVid.layoutManager = LinearLayoutManager(this)
        binding.rvOtherVid.adapter = videoAdapter

        // Fetch and display related videos
        val playlistKey = intent.getStringExtra(EXTRA_PLAYLIST_KEY)
        if (playlistKey != null) {
            fetchPlaylistVideos(playlistKey)
        } else {
            Toast.makeText(this, "Playlist not found", Toast.LENGTH_SHORT).show()
        }

        // Full-screen toggle
        binding.icnFullScreen.setOnClickListener {
            toggleFullScreen()
        }
    }

    private fun fetchPlaylistVideos(playlistKey: String) {
        lifecycleScope.launch {
            try {
                val map = RetrofitClient.apiService.getPlaylists()
                val videos = map[playlistKey] ?: emptyList()
                videoAdapter.submitList(videos)
            } catch (e: Exception) {
                Toast.makeText(this@VideoPlayerActivity, "Error loading videos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun playVideo(item: com.example.hindianimetv.data.model.VideoItem) {
        val videoId = YoutubeUtils.extractYoutubeId(item.videoUrl)
        if (videoId != null) {
            youTubePlayer?.loadVideo(videoId, 0f)
            binding.vidTitle.text = item.title
            binding.vidPublisher.text = "$pub ${item.publisher}"
        } else {
            Toast.makeText(this, "Invalid video id", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun toggleFullScreen() {
        isFullScreen = !isFullScreen
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        if (isFullScreen) {
            WindowCompat.setDecorFitsSystemWindows(window, false)

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            windowInsetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            // Hide other UI elements
            binding.vidTitle.visibility = View.GONE
            binding.vidPublisher.visibility = View.GONE
            binding.rvOtherVid.visibility = View.GONE

            // Change fullscreen icon
            binding.icnFullScreen.setImageResource(R.drawable.ic_full_screen_exit)

            val params = binding.youtubePlayerView.layoutParams as ConstraintLayout.LayoutParams
            params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
            params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
            params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            binding.youtubePlayerView.layoutParams = params

        } else {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())

            // Show other UI elements
            binding.vidTitle.visibility = View.VISIBLE
            binding.vidPublisher.visibility = View.VISIBLE
            binding.rvOtherVid.visibility = View.VISIBLE

            // Change fullscreen icon back
            binding.icnFullScreen.setImageResource(R.drawable.ic_full_screen)

            // Restore YouTubePlayerView size
            val params = binding.youtubePlayerView.layoutParams as ConstraintLayout.LayoutParams
            params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
            params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            params.topToTop = ConstraintLayout.LayoutParams.UNSET
            params.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            binding.youtubePlayerView.layoutParams = params
        }

        // Keep playing at the same position (optional)
        youTubePlayer?.loadVideo(
            intent.getStringExtra(EXTRA_VIDEO_ID) ?: return,
            currentPlaybackPosition
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(binding.youtubePlayerView)
    }
}




/*
package com.example.hindianimetv.ui.activity

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hindianimetv.R
import com.example.hindianimetv.data.network.RetrofitClient
import com.example.hindianimetv.databinding.ActivityVideoPlayerBinding
import com.example.hindianimetv.ui.adapter.EnglishVideoAdapter
import com.example.hindianimetv.util.YoutubeUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.transition.Visibility

class VideoPlayerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_VIDEO_ID = "extra_video_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_PUBLISHER = "extra_publisher"
        const val EXTRA_PLAYLIST_KEY = "extra_playlist_key"
    }

    private lateinit var binding: ActivityVideoPlayerBinding
    private val videoAdapter by lazy { EnglishVideoAdapter { playVideo(it) } }
    private var youTubePlayer: YouTubePlayer? = null
    private var isFullScreen = false
    private var currentPlaybackPosition = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize YouTubePlayerView
        lifecycle.addObserver(binding.youtubePlayerView)
        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@VideoPlayerActivity.youTubePlayer = youTubePlayer
                val videoId = intent.getStringExtra(EXTRA_VIDEO_ID)
                if (videoId != null) {
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            }
        })

        // Setup title and publisher
        val title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        val publisher = intent.getStringExtra(EXTRA_PUBLISHER) ?: ""
        binding.vidTitle.text = title
        binding.vidPublisher.text = "Upload By: $publisher"

        // Setup rvOtherVid
        binding.rvOtherVid.layoutManager = LinearLayoutManager(this)
        binding.rvOtherVid.adapter = videoAdapter

        // Fetch and display related videos
        val playlistKey = intent.getStringExtra(EXTRA_PLAYLIST_KEY)
        if (playlistKey != null) {
            fetchPlaylistVideos(playlistKey)
        } else {
            Toast.makeText(this, "Playlist not found", Toast.LENGTH_SHORT).show()
        }

        // Full-screen toggle
        binding.icnFullScreen.setOnClickListener {
            toggleFullScreen()
        }
    }

    private fun fetchPlaylistVideos(playlistKey: String) {
        lifecycleScope.launch {
            try {
                val map = RetrofitClient.apiService.getPlaylists()
                val videos = map[playlistKey] ?: emptyList()
                videoAdapter.submitList(videos)
            } catch (e: Exception) {
                Toast.makeText(this@VideoPlayerActivity, "Error loading videos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun playVideo(item: com.example.hindianimetv.data.model.VideoItem) {
        val videoId = YoutubeUtils.extractYoutubeId(item.videoUrl)
        if (videoId != null) {
            youTubePlayer?.loadVideo(videoId, 0f)
            binding.vidTitle.text = item.title
            binding.vidPublisher.text = "Upload By: ${item.publisher}"
        } else {
            Toast.makeText(this, "Invalid video id", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toggleFullScreen() {
        isFullScreen = !isFullScreen
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        if (isFullScreen) {
            // Enter full-screen
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            // Hide other UI elements
            binding.vidTitle.visibility = GONE
            binding.vidPublisher.visibility = GONE
            binding.rvOtherVid.visibility = GONE
            binding.icnFullScreen.setImageResource(R.drawable.ic_full_screen_exit)

            // Make YouTubePlayerView fill the screen
            binding.youtubePlayerView.layoutParams = binding.youtubePlayerView.layoutParams.apply {
                height = 0 // Match constraint dimension
                app:layout_constraintBottom_toBottomOf = "parent"
            }
        } else {
            // Exit full-screen
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())

            // Show other UI elements
            binding.vidTitle.visibility = VISIBLE
            binding.vidPublisher.visibility = VISIBLE
            binding.rvOtherVid.visibility = VISIBLE
            binding.icnFullScreen.setImageResource(R.drawable.ic_full_screen)

            // Restore YouTubePlayerView to wrap_content
            binding.youtubePlayerView.layoutParams = binding.youtubePlayerView.layoutParams.apply {
                height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                app:layout_constraintBottom_toBottomOf = null
            }
        }

        // Resume playback at the current position
        youTubePlayer?.loadVideo(intent.getStringExtra(EXTRA_VIDEO_ID) ?: return, currentPlaybackPosition)
    }


    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(binding.youtubePlayerView)
    }
}*/
