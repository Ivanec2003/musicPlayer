package com.example.music.mvvm.data_base

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.music.mvvm.model.SongModel

class DatabaseFavoriteSongs(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "FavoriteSongs.db"
        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${FavoriteSongContact.TABLE_NAME} (" +
                    "${FavoriteSongContact.COLUMN_NAME_TITLE} TEXT," +
                    "${FavoriteSongContact.COLUMN_NAME_ARTIST} TEXT," +
                    "${FavoriteSongContact.COLUMN_NAME_IS_FAVORITE} INTEGER)"
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FavoriteSongContact.TABLE_NAME}"
    }

    fun insertFavoriteSong(song: SongModel) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(FavoriteSongContact.COLUMN_NAME_TITLE, song.title)
            put(FavoriteSongContact.COLUMN_NAME_ARTIST, song.artist)
            put(FavoriteSongContact.COLUMN_NAME_IS_FAVORITE, if (song.statusFavorites) 1 else 0)
        }
        db.insert(FavoriteSongContact.TABLE_NAME, null, values)
        db.close()
    }

    fun deleteFavoriteSong(song: SongModel) {
        val db = writableDatabase
        val selection = "${FavoriteSongContact.COLUMN_NAME_TITLE} = ? AND ${FavoriteSongContact.COLUMN_NAME_ARTIST} = ?"
        val selectionArgs = arrayOf(song.title, song.artist)
        db.delete(FavoriteSongContact.TABLE_NAME, selection, selectionArgs)
        db.close()
    }

    fun getIndexFavoriteSongs(listSong: MutableList<SongModel>): List<Int> {

        val db = readableDatabase
        val listIndexElements = mutableListOf<Int>()
        val projection = arrayOf(
            FavoriteSongContact.COLUMN_NAME_TITLE,
            FavoriteSongContact.COLUMN_NAME_ARTIST,
        )

        val cursor = db.query(
            FavoriteSongContact.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        with(cursor) {
            while (moveToNext()) {
                val title = getString(getColumnIndexOrThrow(FavoriteSongContact.COLUMN_NAME_TITLE))
                val artist = getString(getColumnIndexOrThrow(FavoriteSongContact.COLUMN_NAME_ARTIST))

                val index = listSong.indexOfFirst { it.title.equals(title) && it.artist.equals(artist) }
                if (index != -1) {
                    listIndexElements.add(index)
                }

            }
        }

        return listIndexElements
    }
}