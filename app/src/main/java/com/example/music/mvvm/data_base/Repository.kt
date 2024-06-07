package com.example.music.mvvm.data_base


import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.music.mvvm.model.SongModel
import java.io.File

class Repository(private val context: Context, private val fragment: Fragment) {

    private var deleteRequestLauncher: ActivityResultLauncher<IntentSenderRequest>? = null

    init {
        initializeDeleteRequestLauncher(fragment)
    }

    private fun initializeDeleteRequestLauncher(fragment: Fragment) {
        deleteRequestLauncher = fragment.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                Log.d("Repository", "File deleted successfully")
            } else {
                Log.d("Repository", "File deletion failed or was cancelled")
            }
        }
    }

    fun getAudioFiles(): List<SongModel> {

        val listAudioFiles = mutableListOf<SongModel>()

        val cursor = settingQuery(context)
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

                val song = SongModel(
                    id,
                    title,
                    artist,
                    album,
                    albumImage.toString(),
                    duration,
                    path,
                    false
                )

                listAudioFiles.add(song)
            }

            if(listAudioFiles.isNotEmpty()){
                val databaseFavoriteSongs = DatabaseFavoriteSongs(context)
                val list = databaseFavoriteSongs.getIndexFavoriteSongs(listAudioFiles)
                if(list.isNotEmpty()){
                    list.forEach{
                        listAudioFiles[it].statusFavorites = true
                    }
                }
            }
        }

        return listAudioFiles
    }

    private fun settingQuery(context: Context): Cursor? {
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
    fun isSongDeletedFromMediaStore(songId: Long): Boolean {
        val contentResolver = context.contentResolver
        val songUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId)
        val cursor = contentResolver.query(songUri, null, null, null, null)
        cursor?.use {
            return it.count == 0
        }
        return false
    }
    fun deleteAudioFiles(songId: Long, path: String) {


        val contentResolver = context.contentResolver
        val songUri =
            ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,songId)

        try {
            contentResolver.delete(songUri, "null", null)
            deleteAudioFileFromStorage(path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun deleteAudioFileFromStorage(path: String): Boolean {
        val file = File(path)
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }

    fun updateFavoriteStatus(song: SongModel) {
        val databaseFavoriteSongs = DatabaseFavoriteSongs(context)

        if(!song.statusFavorites)
            databaseFavoriteSongs.insertFavoriteSong(song)
        else
            databaseFavoriteSongs.deleteFavoriteSong(song)

    }

    private fun getUriAlbum(albumId: Long): Uri {
        val albumArtUri = Uri.parse("content://media/external/audio/albumart")
        return ContentUris.withAppendedId(albumArtUri, albumId)
    }

}