package com.example.music.mvvm.model

import android.net.Uri

data class SongModel(
    val id: Long?,
    val title: String?,
    val artist: String?,
    val album: String?,
    val image: String?,
    val duration: Long?,
    val path: String?,
    var statusFavorites: Boolean,
    val uri: Uri
)
