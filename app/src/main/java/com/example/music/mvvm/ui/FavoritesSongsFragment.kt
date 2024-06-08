package com.example.music.mvvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.music.databinding.FragmentFavoritesSongsBinding
import com.example.music.mvvm.adapter.ListSongsAdapter
import com.example.music.mvvm.view_model.CommonViewModelFactory

class FavoritesSongsFragment : Fragment() {
    private var _binding: FragmentFavoritesSongsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoritesSongsBinding.inflate(inflater, container, false)
        return binding.root
    }
}