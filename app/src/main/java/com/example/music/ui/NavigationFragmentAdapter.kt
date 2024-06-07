package com.example.music.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.music.mvvm.albums.AlbumsFragment
import com.example.music.mvvm.folders.FoldersFragment
import com.example.music.mvvm.recognition.RecognitionSongsFragment
import com.example.music.mvvm.favorites_songs.FavoritesSongsFragment
import com.example.music.mvvm.songs.SongsFragment

class NavigationFragmentAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {

        return when(position) {
            0 -> RecognitionSongsFragment()
            1 -> SongsFragment()
            2 -> AlbumsFragment()
            3 -> FoldersFragment()
            4 -> FavoritesSongsFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }

    }
}