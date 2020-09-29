package com.logicielhouse.ca.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.logicielhouse.ca.fragments.BasketballFragment
import com.logicielhouse.ca.fragments.FootballFragment
import com.logicielhouse.ca.fragments.OthersFragment

/**
 * Created by Abdullah on 9/26/2020.
 */
class TableViewpagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FootballFragment()
            1 -> BasketballFragment()
            2 -> BasketballFragment()
            else -> OthersFragment()
        }
    }
}