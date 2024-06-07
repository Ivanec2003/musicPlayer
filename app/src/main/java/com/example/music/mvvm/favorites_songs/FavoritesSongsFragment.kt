package com.example.music.mvvm.favorites_songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.music.R
import com.example.music.databinding.FragmentFavoritesSongsBinding
import com.example.music.mvvm.adapter.ListSongsAdapter
import com.example.music.mvvm.data_base.Repository
import com.example.music.mvvm.model.SongModel
import com.example.music.mvvm.music_player.MusicPlayerFragment
import com.example.music.mvvm.view_model.PlayerViewModel
import com.example.mymusic.mvvm.songs.SongsFactory

class FavoritesSongsFragment : Fragment() {
    private var _binding: FragmentFavoritesSongsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PlayerViewModel
    private lateinit var adapter: ListSongsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        initViewModel()
        viewModel.fetchListSong()

        _binding = FragmentFavoritesSongsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        viewModel.listSongs.observe(viewLifecycleOwner){listSongs->
            val favoriteSongs = listSongs.filter { it.statusFavorites }
            adapter.submitList(favoriteSongs)
        }
    }

    private fun initAdapter(){
        adapter = ListSongsAdapter(
            onItemClick = {song ->
            clickOnItemListSong(song)
            },
            onMoreButtonClick = {song, view ->
                clickOnMenuMore(song)
            })
        binding.recyclerViewFavoriteSongs.adapter = adapter
        binding.recyclerViewFavoriteSongs.layoutManager = LinearLayoutManager(requireActivity())
        val dividerItemDecoration = DividerItemDecoration(binding.recyclerViewFavoriteSongs.context, LinearLayoutManager.VERTICAL)
        binding.recyclerViewFavoriteSongs.addItemDecoration(dividerItemDecoration)
    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(
            this,
            SongsFactory(Repository(requireContext(), this))
        )[PlayerViewModel::class.java]
    }
    private fun clickOnItemListSong(song: SongModel){
        parentFragmentManager.beginTransaction()
            .addToBackStack("")
            .replace(R.id.fragment_container, MusicPlayerFragment())
            .commit()
    }
    private fun clickOnMenuMore(song: SongModel){
    }

}