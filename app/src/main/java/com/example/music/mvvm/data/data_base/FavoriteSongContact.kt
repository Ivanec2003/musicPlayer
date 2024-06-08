package com.example.music.mvvm.data.data_base

import android.provider.BaseColumns

object FavoriteSongContact: BaseColumns {
        const val TABLE_NAME = "FavoritesSong"
        const val COLUMN_NAME_TITLE = "Title"
        const val COLUMN_NAME_ARTIST = "Artist"
        const val COLUMN_NAME_ALBUM = "Album"

}