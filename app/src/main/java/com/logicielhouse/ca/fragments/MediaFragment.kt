package com.logicielhouse.ca.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.logicielhouse.ca.R
import com.logicielhouse.ca.adapter.MultimediaViewpagerAdapter
import com.logicielhouse.ca.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_media.*

class MediaFragment : Fragment(R.layout.fragment_media) {
    companion object {
        fun newInstance(): MediaFragment = MediaFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).bottomNavigation.menu.getItem(0).isChecked = true
        (activity as MainActivity).setToolbarTitle( getString(R.string.multimedia))

        viewPagerMedia.apply {
            isUserInputEnabled = true
            adapter = MultimediaViewpagerAdapter(this@MediaFragment)
            offscreenPageLimit = 1
        }

        TabLayoutMediator(tabLayoutMedia, viewPagerMedia) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.videos)
                1 -> tab.text = getString(R.string.photos)
                else -> tab.text = getString(R.string.songs)
            }
        }.attach()
    }
}