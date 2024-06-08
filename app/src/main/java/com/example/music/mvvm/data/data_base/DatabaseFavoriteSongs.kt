package com.example.music.mvvm.data.data_base

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
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
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "FavoriteSongs.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${FavoriteSongContact.TABLE_NAME} (" +
                    "${FavoriteSongContact.COLUMN_NAME_TITLE} TEXT," +
                    "${FavoriteSongContact.COLUMN_NAME_ARTIST} TEXT," +
                    "${FavoriteSongContact.COLUMN_NAME_ALBUM} TEXT)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FavoriteSongContact.TABLE_NAME}"
    }

    fun insertFavoriteSong(song: SongModel) {
        val db = writableDatabase
        val checkFavoriteSongs = !song.statusFavorites

        if(checkFavoriteSongs){

            val values = ContentValues().apply {
                put(FavoriteSongContact.COLUMN_NAME_TITLE, song.title)
                put(FavoriteSongContact.COLUMN_NAME_ARTIST, song.artist)
                put(FavoriteSongContact.COLUMN_NAME_ALBUM, song.album)
            }

            db.insert(FavoriteSongContact.TABLE_NAME, null, values)

        }else
            deleteFavoriteSong(song)

        db.close()
    }

    fun getFavoriteListSongs(listSong: MutableList<SongModel>): MutableList<SongModel> {

        val db = readableDatabase
        val saveList = listSong

        val cursor = settingCursor(db)

        with(cursor) {
            while (moveToNext()) {
                val title = getString(getColumnIndexOrThrow(FavoriteSongContact.COLUMN_NAME_TITLE))
                val artist = getString(getColumnIndexOrThrow(FavoriteSongContact.COLUMN_NAME_ARTIST))
                val album = getString(getColumnIndexOrThrow(FavoriteSongContact.COLUMN_NAME_ALBUM))


                listSong.forEach {
                    if (it.title == title && it.artist == artist && it.album == album) {
                        it.statusFavorites = true
                    }
                }
                checkRevelenseDB(listSong, saveList, cursor)
            }
        }

        return listSong
    }
    private fun checkRevelenseDB(listSong: List<SongModel>, saveList: List<SongModel>, cursor: Cursor){
        if(listSong == saveList){
            val db = writableDatabase  // Передбачається, що у вас є посилання на об'єкт бази даних

            val titleIndex = cursor.getColumnIndexOrThrow(FavoriteSongContact.COLUMN_NAME_TITLE)
            val artistIndex = cursor.getColumnIndexOrThrow(FavoriteSongContact.COLUMN_NAME_ARTIST)
            val albumIndex = cursor.getColumnIndexOrThrow(FavoriteSongContact.COLUMN_NAME_ALBUM)

            val title = cursor.getString(titleIndex)
            val artist = cursor.getString(artistIndex)
            val album = cursor.getString(albumIndex)

            val selection = "${FavoriteSongContact.COLUMN_NAME_TITLE} = ? AND ${FavoriteSongContact.COLUMN_NAME_ARTIST} = ? AND ${FavoriteSongContact.COLUMN_NAME_ALBUM} = ?  "
            val selectionArgs = arrayOf(title, artist, album)
            db.delete(FavoriteSongContact.TABLE_NAME, selection, selectionArgs)

            db.close()
        }

    }
    fun deleteFavoriteSong(song: SongModel) {
        val db = writableDatabase
        val selection = "${FavoriteSongContact.COLUMN_NAME_TITLE} = ? AND ${FavoriteSongContact.COLUMN_NAME_ARTIST} = ? AND ${FavoriteSongContact.COLUMN_NAME_ALBUM} = ?  "
        val selectionArgs = arrayOf(song.title, song.artist, song.album)
        db.delete(FavoriteSongContact.TABLE_NAME, selection, selectionArgs)
        db.close()
    }

    private fun settingCursor(db: SQLiteDatabase): Cursor{
        val projection = arrayOf(
            FavoriteSongContact.COLUMN_NAME_TITLE,
            FavoriteSongContact.COLUMN_NAME_ARTIST,
            FavoriteSongContact.COLUMN_NAME_ALBUM
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
        return cursor
    }
}