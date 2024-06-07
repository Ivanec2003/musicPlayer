package com.example.music.mvvm.model

data class SongModel(
    val id: Long?,
    val title: String?,
    val artist: String?,
    val album: String?,
    val image: String?,
    val duration: Long?,
    val path: String?,
    var statusFavorites: Boolean)
