package com.example.hindianimetv.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hindianimetv.R
import com.example.hindianimetv.data.model.EpisodeInfo
import com.example.hindianimetv.data.model.VideoItem
import com.example.hindianimetv.data.network.RetrofitClient
import com.example.hindianimetv.data.repository.PlaylistRepository
import com.example.hindianimetv.databinding.ActivityMainBinding
import com.example.hindianimetv.ui.adapter.EnglishVideoAdapter
import com.example.hindianimetv.ui.adapter.EpisodeAdapter
import com.example.hindianimetv.ui.adapter.HindiVideoAdapter
import com.example.hindianimetv.util.YoutubeUtils
import com.example.hindianimetv.viewmodel.PlaylistViewModel
import com.example.hindianimetv.viewmodel.PlaylistViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: PlaylistViewModel by viewModels {
        PlaylistViewModelFactory(PlaylistRepository(RetrofitClient.apiService))
    }

    private val hindiAdapter by lazy { HindiVideoAdapter { openPlayer(it, "hindi_anime") } }
    private val englishAdapter by lazy { EnglishVideoAdapter { openPlayer(it, "english_anime") } }
    private val episodeAdapter by lazy { EpisodeAdapter { openEpisode(it) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge()
        initToolbarMenu()
        setupRecyclerViews()
        setupObservers()

        viewModel.loadPlaylists()
        setupViewAllButtons()
    }

    private fun setupEdgeToEdge() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun initToolbarMenu() {
        binding.toolBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.rate -> {
                    openPlayStore(); true
                }
                R.id.share -> {
                    shareApp(); true
                }
                R.id.darkmode -> {
                    toggleDarkMode(); true
                }
                R.id.privacy_policy -> {
                    openWebPage("https://rekibimages.blogspot.com/2025/10/privacy-policy-hindi-anime-tv.html"); true
                }
                R.id.disclaimer -> {
                    openWebPage("https://rekibimages.blogspot.com/2025/10/disclaimer-hindi-anime-tv.html"); true
                }
                R.id.contactus -> {
                    openWebPage("https://rekibimages.blogspot.com/2025/10/contact-us-hindi-anime-tv.html"); true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerViews() {
        binding.rvHindi.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = hindiAdapter
        }

        binding.rvEnglish.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = englishAdapter
        }

        binding.rvEpisodes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = episodeAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupViewAllButtons() {
        binding.viewDubbedAll.setOnClickListener {
            EpisodesPlaylistActivity.start(this, "english_anime")
        }
        binding.viewLatestAll.setOnClickListener {
            EpisodesPlaylistActivity.start(this, "hindi_anime")
        }
    }

    // ViewModel
    private fun setupObservers() {
        viewModel.playlists.observe(this) { map ->
            hindiAdapter.submitList(map["hindi_anime"].orEmpty())

            val englishList = map["english_anime"].orEmpty()
            englishAdapter.submitList(englishList.take(4))

            val episodes = map
                .filterKeys { it != "hindi_anime" && it != "english_anime" }
                .map { (key, videos) ->
                    val thumb = videos.firstOrNull()?.videoUrl?.let { url ->
                        YoutubeUtils.extractYoutubeId(url)?.let { YoutubeUtils.getThumbnailUrlFromVideoId(it) }
                    }
                    EpisodeInfo(
                        key = key,
                        displayTitle = key.replace('_', ' ').replaceFirstChar { it.uppercase() },
                        thumbUrl = thumb,
                        count = videos.size
                    )
                }
                .sortedBy { it.displayTitle }

            episodeAdapter.submitList(episodes)
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openPlayer(item: VideoItem, playlistKey: String) {
        val videoId = YoutubeUtils.extractYoutubeId(item.videoUrl)
        if (videoId == null) {
            Toast.makeText(this, "Invalid video id", Toast.LENGTH_SHORT).show()
            return
        }

        Intent(this, VideoPlayerActivity::class.java).apply {
            putExtra(VideoPlayerActivity.EXTRA_VIDEO_ID, videoId)
            putExtra(VideoPlayerActivity.EXTRA_TITLE, item.title)
            putExtra(VideoPlayerActivity.EXTRA_PUBLISHER, item.publisher)
            putExtra(VideoPlayerActivity.EXTRA_PLAYLIST_KEY, playlistKey)
            startActivity(this)
        }
    }

    private fun openEpisode(episodeInfo: EpisodeInfo) {
        EpisodesPlaylistActivity.start(this, episodeInfo.key)
    }

    private fun openPlayStore() {
        val appPackage = packageName
        try {
            startActivity(Intent(Intent.ACTION_VIEW, "market://details?id=$appPackage".toUri()))
        } catch (_: android.content.ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, "https://play.google.com/store/apps/details?id=$appPackage".toUri()))
        }
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "Hey! Check out this Hindi Anime TV App:\nhttps://play.google.com/store/apps/details?id=$packageName"
            )
        }
        startActivity(Intent.createChooser(shareIntent, "Share App via"))
    }

    private fun toggleDarkMode() {
        val current = AppCompatDelegate.getDefaultNightMode()
        val newMode = if (current == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.MODE_NIGHT_NO
        } else {
            AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(newMode)
    }

    private fun openWebPage(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    }
}