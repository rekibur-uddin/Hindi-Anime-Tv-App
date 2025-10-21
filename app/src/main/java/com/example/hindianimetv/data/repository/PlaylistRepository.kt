package com.example.hindianimetv.data.repository

import com.example.hindianimetv.data.network.ApiService

class PlaylistRepository(private val api: ApiService) {
    suspend fun fetchPlaylists() = api.getPlaylists()
}