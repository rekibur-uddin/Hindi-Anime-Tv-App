package com.example.hindianimetv.data.network

import com.example.hindianimetv.data.model.VideoItem
import retrofit2.http.GET

interface ApiService {
    @GET ("videos.json")
    suspend fun getPlaylists(): Map<String, List<VideoItem>>
}