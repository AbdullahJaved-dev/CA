package com.logicielhouse.ca.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.logicielhouse.ca.MainActivity
import com.logicielhouse.ca.R
import kotlinx.android.synthetic.main.activity_main.*


class MatchesFragment : Fragment(R.layout.fragment_matches) {
    companion object {
        fun newInstance() = MatchesFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).bottomNavigation.menu.getItem(4).isChecked = true
        (activity as MainActivity).setToolbarTitle(getString(R.string.matches))

    }
}