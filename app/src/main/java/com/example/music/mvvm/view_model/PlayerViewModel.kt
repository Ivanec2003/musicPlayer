package com.example.music.mvvm.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music.mvvm.data_base.Repository
import com.example.music.mvvm.model.SongModel
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val repository: Repository
) : ViewModel() {

    private var _listSongs: MutableLiveData<List<SongModel>> = MutableLiveData()

    val listSongs: LiveData<List<SongModel>>
        get() = _listSongs

    fun fetchListSong() {
        if (Data.listSongs.isEmpty()) {
            val list = repository.getAudioFiles()
            _listSongs.value = list
            Data.listSongs = list
        } else
            _listSongs.value = Data.listSongs
    }

    fun deleteSong(songModel: SongModel) {
        if (songModel.id != null && songModel.path != null) {
            viewModelScope.launch {
                repository.deleteAudioFiles(songModel.uri)

                val updatedList = _listSongs.value?.toMutableList()
                updatedList?.remove(songModel)

                _listSongs.value = updatedList
                Data.listSongs = updatedList ?: listOf()
            }
        }
    }

    fun updateFavoriteStatusSong(songModel: SongModel) {
        if (songModel.id != null) {
            repository.updateFavoriteStatus(songModel)
        }
    }

    fun changeNameSong(songModel: SongModel, newName: String){

    }
}