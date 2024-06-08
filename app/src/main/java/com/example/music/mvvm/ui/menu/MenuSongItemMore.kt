package com.example.music.mvvm.ui.menu

import android.app.AlertDialog
import android.content.Context
import android.view.MenuItem
import com.example.music.R
import com.example.music.mvvm.model.SongModel
import com.example.music.mvvm.view_model.CommonViewModelFactory

object MenuSongItemMore {
    private lateinit var viewModelMenu: CommonViewModelFactory

    fun clickMenuItem(context: Context, menuItem: MenuItem, currentSong: SongModel, viewModel: CommonViewModelFactory): Boolean {

        viewModelMenu = viewModel

        when (menuItem.itemId) {
            R.id.menu_addToFavorites -> {
                viewModel.updateFavoriteStatusSong(currentSong)
                return true
            }
            R.id.menu_viewDetail -> {
                showSongDetailsDialog(context, currentSong)
                return true
            }
            R.id.menu_editCancel ->{
                return false
            }
        }
        return false
    }

    private fun showSongDetailsDialog(context: Context, song: SongModel) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("Details")

        val message ="\nSong name: " + song.artist + "\n" +
                "\nArtist: " + song.artist + "\n" +
                "\nDuration: " + formatDuration(song.duration) + "\n" +
                "\nAlbum Name: " + song.album + "\n" +
                "\nPath: " + song.path + "\n"
        dialogBuilder.setMessage(message)

        dialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }
    private fun formatDuration(duration: Long?): String {
        duration?.let {
            val seconds = it / 1000
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            return String.format("%02d:%02d", minutes, remainingSeconds)
        }
        return ""
    }
}