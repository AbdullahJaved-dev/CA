package com.logicielhouse.ca.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.logicielhouse.ca.MainActivity
import com.logicielhouse.ca.R
import com.logicielhouse.ca.adapter.NewsAdapter
import com.logicielhouse.ca.model.NewsModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_view_media.*
import kotlinx.android.synthetic.main.fragment_news.*


class NewsFragment : Fragment(R.layout.fragment_news) {
    companion object {
        fun newInstance() = NewsFragment()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).bottomNavigation.menu.getItem(1).isChecked = true
        (activity as MainActivity).setToolbarTitle(getString(R.string.news))

        rvNews.apply {
            adapter = NewsAdapter(listener = object : NewsAdapter.NewsAdapterClickListener {
                override fun viewPicture(newsItem: NewsModel) {
                }

                override fun playVideo(newsItem: NewsModel) {
                }

            }, NewsModel.newsList)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }
}