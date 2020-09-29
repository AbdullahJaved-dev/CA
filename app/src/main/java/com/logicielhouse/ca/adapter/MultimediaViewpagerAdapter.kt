package com.logicielhouse.ca.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.logicielhouse.ca.fragments.PicturesFragment
import com.logicielhouse.ca.fragments.SongsFragment
import com.logicielhouse.ca.fragments.VideosFragment

/**
 * Created by Abdullah on 9/26/2020.
 */
class MultimediaViewpagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> VideosFragment()
            1 -> PicturesFragment()
            else -> SongsFragment()
        }
    }
}