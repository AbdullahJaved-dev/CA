package com.logicielhouse.ca.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.logicielhouse.ca.MainActivity
import com.logicielhouse.ca.R
import com.logicielhouse.ca.adapter.TableViewpagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_table.*

class TableFragment : Fragment(R.layout.fragment_table) {
    companion object {
        fun newInstance() = TableFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).bottomNavigation.menu.getItem(3).isChecked = true
        (activity as MainActivity).setToolbarTitle(getString(R.string.table))


        viewPagerTable.apply {
            isUserInputEnabled = true
            adapter = TableViewpagerAdapter(this@TableFragment)
        }

        TabLayoutMediator(tabLayoutTable, viewPagerTable) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.football)
                1 -> tab.text = getString(R.string.basketball)
                2 -> tab.text = getString(R.string.handball)
                else -> tab.text = getString(R.string.others)
            }
        }.attach()
    }

}