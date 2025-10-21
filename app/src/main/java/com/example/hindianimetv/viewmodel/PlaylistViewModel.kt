package com.example.hindianimetv.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hindianimetv.data.model.VideoItem
import com.example.hindianimetv.data.repository.PlaylistRepository
import kotlinx.coroutines.launch

class PlaylistViewModel(private val repo: PlaylistRepository): ViewModel(){
    private val _playlists = MutableLiveData<Map<String, List<VideoItem>>>()
    val playlists: LiveData<Map<String, List<VideoItem>>> = _playlists

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadPlaylists() {
        viewModelScope.launch {
            try {
                val data = repo.fetchPlaylists()
                _playlists.postValue(data)
            } catch (e: Exception) {
                _error.postValue(e.message?: "Technical Error")
            }
        }
    }
}