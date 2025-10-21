package com.example.hindianimetv.data.model
data class EpisodeInfo(
    val key: String,         // e.g. "episode_1"
    val displayTitle: String,// e.g. "Episode 1"
    val thumbUrl: String?,   // optional thumbnail (from first video)
    val count: Int           // number of videos in this episode
)