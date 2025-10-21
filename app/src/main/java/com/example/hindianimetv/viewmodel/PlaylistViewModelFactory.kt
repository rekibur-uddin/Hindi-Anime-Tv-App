package com.example.hindianimetv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hindianimetv.data.repository.PlaylistRepository

class PlaylistViewModelFactory(private val repo: PlaylistRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaylistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlaylistViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}