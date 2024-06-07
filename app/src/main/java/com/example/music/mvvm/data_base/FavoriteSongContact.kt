package com.example.music.mvvm.data_base

import android.provider.BaseColumns

object FavoriteSongContact: BaseColumns {
        const val TABLE_NAME = "songs"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_ARTIST = "artist"
        const val COLUMN_NAME_IS_FAVORITE = "is_favorite"
}