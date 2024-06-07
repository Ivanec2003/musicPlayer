package com.example.music.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.music.R
import com.example.music.databinding.FragmentNavigationBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class NavigationFragment : Fragment() {

    private var _binding: FragmentNavigationBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterNavigation: NavigationFragmentAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNavigationBinding.inflate(inflater, container, false)
        initViewPager(binding.root)
        return binding.root
    }

    private fun initViewPager(view: View) {
        adapterNavigation = NavigationFragmentAdapter(requireActivity())

        viewPager = view.findViewById(R.id.viewPager)
        viewPager.adapter = adapterNavigation
        viewPager.currentItem = 1

        tabLayout = view.findViewById(R.id.tabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Recognition"
                1 -> tab.text = "Songs"
                2 -> tab.text = "Albums"
                3 -> tab.text = "Folders"
                4 -> tab.text = "Favorites"
                else -> tab.text = "Tab ${position + 1}"
            }
        }.attach()
    }

}