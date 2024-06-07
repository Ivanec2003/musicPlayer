package com.example.music.mvvm.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.music.R
import com.example.music.databinding.FragmentSongsBinding
import com.example.music.mvvm.adapter.ListSongsAdapter
import com.example.music.mvvm.data_base.Repository
import com.example.music.mvvm.menu.MenuSongItemMore
import com.example.music.mvvm.model.SongModel
import com.example.music.mvvm.music_player.MusicPlayerFragment
import com.example.music.mvvm.view_model.PlayerViewModel
import com.example.mymusic.mvvm.songs.SongsFactory
import com.example.yourapp.utils.PermissionHandler

class SongsFragment : Fragment() {
    private var _binding: FragmentSongsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PlayerViewModel
    private lateinit var adapter: ListSongsAdapter

    /*private lateinit var fileHelper: FileHelper*/
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = initViewModel()
        viewModel.fetchListSong()

        _binding = FragmentSongsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                PermissionHandler.handlePermissionResult(
                    isGranted,
                    requireContext(),
                    requestPermissionLauncher
                )
            }

        initPermissionLauncher()
        getPermission()

        initAdapter()
        viewModel.listSongs.observe(viewLifecycleOwner) { listSongs ->
            adapter.submitList(listSongs)
        }

        /*fileHelper = FileHelper(this)*/

    }

    private fun initViewModel(): PlayerViewModel {
        return ViewModelProvider(
            this,
            SongsFactory(Repository(requireContext(), this))
        )[PlayerViewModel::class.java]
    }

    private fun initAdapter() {
        adapter = ListSongsAdapter(
            onItemClick = { song ->
                clickOnItemListSong(song)
            },
            onMoreButtonClick = { song, view ->
                clickOnMenuMore(song, view)
            }
        )
        binding.recyclerViewSongs.adapter = adapter
        binding.recyclerViewSongs.layoutManager = LinearLayoutManager(requireActivity())
        val dividerItemDecoration =
            DividerItemDecoration(binding.recyclerViewSongs.context, LinearLayoutManager.VERTICAL)
        binding.recyclerViewSongs.addItemDecoration(dividerItemDecoration)
    }

    private fun clickOnItemListSong(song: SongModel) {
        parentFragmentManager.beginTransaction().addToBackStack("")
            .replace(R.id.fragment_container, MusicPlayerFragment()).commit()
    }

    private fun clickOnMenuMore(song: SongModel, view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_item_song, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            MenuSongItemMore.clickMenuItem(
                requireContext(),
                menuItem,
                song,
                viewModel/*, fileHelper*/
            )
        }

        popupMenu.show()
    }

    private fun initPermissionLauncher() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                PermissionHandler.handlePermissionResult(
                    isGranted,
                    requireContext(),
                    requestPermissionLauncher
                )
            }
    }

    private fun getPermission() {
        PermissionHandler.checkAndRequestPermission(requireContext(), requestPermissionLauncher)
    }

}