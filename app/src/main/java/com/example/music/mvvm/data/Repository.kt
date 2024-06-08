package com.example.music.mvvm.data

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.music.mvvm.data.data_base.DatabaseFavoriteSongs
import com.example.music.mvvm.model.SongModel


class Repository(private val context: Context) {
    companion object{
        var saveListSong: List<SongModel> = emptyList()
    }

    fun getAudioFiles(){

        var listAudioFiles = mutableListOf<SongModel>()
        val cursor = settingCursor(context)

        cursor?.use {

            val idIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val albumIdIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val durationIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val pathIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idIndex)
                val title = cursor.getString(titleIndex)
                val artist = cursor.getString(artistIndex)
                val album = cursor.getString(albumIndex)
                val albumId = cursor.getLong(albumIdIndex)
                val albumImage = getUriAlbum(albumId)
                val duration = cursor.getLong(durationIndex)
                val path = cursor.getString(pathIndex)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id)

                val song = SongModel(
                    id,
                    title,
                    artist,
                    album,
                    albumImage.toString(),
                    duration,
                    path,
                    false,
                    contentUri
                )

                listAudioFiles.add(song)
            }
        }

        if(listAudioFiles.isNotEmpty()){
            val databaseFavoriteSongs = DatabaseFavoriteSongs(context)
            listAudioFiles = databaseFavoriteSongs.getFavoriteListSongs(listAudioFiles)
        }

        saveListSong = listAudioFiles
    }

    private fun settingCursor(context: Context): Cursor? {
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )

        val selection = MediaStore.Audio.Media.IS_MUSIC

        val audioCollection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL_PRIMARY
                )
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

        return context.contentResolver.query(
            audioCollection,
            projection,
            selection,
            null,
            null
        )
    }

    private fun getUriAlbum(albumId: Long): Uri {
        val albumArtUri = Uri.parse("content://media/external/audio/albumart")
        return ContentUris.withAppendedId(albumArtUri, albumId)
    }

    fun updateFavoriteStatus(song: SongModel) {
        val databaseFavoriteSongs = DatabaseFavoriteSongs(context)

        if(!song.statusFavorites)
            databaseFavoriteSongs.insertFavoriteSong(song)
        else
            databaseFavoriteSongs.deleteFavoriteSong(song)

    }


}