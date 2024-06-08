package com.example.music.mvvm.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music.mvvm.data.Repository
import com.example.music.mvvm.model.SongModel
import kotlinx.coroutines.launch

class CommonViewModelFactory(
    private val repository: Repository
) : ViewModel() {

    private var _listSongs: MutableLiveData<List<SongModel>> = MutableLiveData()

    val listSongs: LiveData<List<SongModel>>
        get() = _listSongs

    fun fetchListSong() {
        viewModelScope.launch {
            if (Repository.saveListSong.isEmpty()) {
                repository.getAudioFiles()
                _listSongs.value = Repository.saveListSong
            } else
                _listSongs.value = Repository.saveListSong
        }
    }

    fun updateFavoriteStatusSong(songModel: SongModel) {
        if (songModel.id != null) {
            repository.updateFavoriteStatus(songModel)
        }

    }
}