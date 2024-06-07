package com.example.mymusic.mvvm.songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.music.mvvm.data_base.Repository
import com.example.music.mvvm.view_model.PlayerViewModel

class SongsFactory(
    private val repository: Repository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java))
            return PlayerViewModel(repository) as T
        throw IllegalArgumentException("Unknown model")
    }
}