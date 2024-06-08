package com.example.music.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.music.R
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    private lateinit var adapterNavigation: NavigationFragmentAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment()
    }
    private fun loadFragment(){
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, NavigationFragment())
            .commit()
    }
}