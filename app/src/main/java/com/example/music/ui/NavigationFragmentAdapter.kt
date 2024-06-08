package com.example.music.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.music.mvvm.ui.AlbumsFragment
import com.example.music.mvvm.ui.FoldersFragment
import com.example.music.mvvm.ui.RecognitionSongsFragment
import com.example.music.mvvm.ui.FavoritesSongsFragment
import com.example.music.mvvm.ui.ListSongsFragment

class NavigationFragmentAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {

        return when(position) {
            0 -> RecognitionSongsFragment()
            1 -> ListSongsFragment()
            2 -> AlbumsFragment()
            3 -> FoldersFragment()
            4 -> FavoritesSongsFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }

    }
}