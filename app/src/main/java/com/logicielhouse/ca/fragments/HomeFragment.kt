package com.logicielhouse.ca.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.logicielhouse.ca.MainActivity
import com.logicielhouse.ca.R
import com.logicielhouse.ca.adapter.NewsAdapter
import com.logicielhouse.ca.adapter.TablePointsAdapter
import com.logicielhouse.ca.adapter.VideosAdapter
import com.logicielhouse.ca.model.NewsModel
import com.logicielhouse.ca.model.TablePointsModel
import com.logicielhouse.ca.model.VideosModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(R.layout.fragment_home), View.OnClickListener {

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupClickListeners()

    }

    private fun setupClickListeners() {
        tvViewAllVideos.setOnClickListener(this)
        tvViewAllNews.setOnClickListener(this)
        tvViewFullTable.setOnClickListener(this)
    }

    private fun setupUI() {
        (activity as MainActivity).bottomNavigation.menu.getItem(2).isChecked = true
        (activity as MainActivity).setToolbarTitle(getString(R.string.home))
        var videosList = ArrayList<VideosModel>()
        var newsList = ArrayList<NewsModel>()
        var pointsList = ArrayList<TablePointsModel>()
        var i = 0
        var j = 0
        var k = 0
        if (VideosModel.videosList.size > 4) {
            while (i < 4) {
                videosList.add(VideosModel.videosList[i])
                i++
            }
        } else {
            videosList = VideosModel.videosList as ArrayList<VideosModel>
        }
        if (NewsModel.newsList.size > 4) {
            while (j < 4) {
                newsList.add(NewsModel.newsList[j])
                j++
            }
        } else {
            newsList = NewsModel.newsList as ArrayList<NewsModel>
        }
        if (TablePointsModel.pointsList.size > 5) {
            while (k < 5) {
                pointsList.add(TablePointsModel.pointsList[k])
                k++
            }
        } else {
            pointsList = TablePointsModel.pointsList as ArrayList<TablePointsModel>
        }

        val layoutManagerHorizontal =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerHorizontal1 =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        rvHomeVideos.apply {
            adapter = VideosAdapter(listener = object : VideosAdapter.VideosAdapterClickListeners {
                override fun playVideo(videoItem: VideosModel) {

                }

            }, videosList, "home")
            layoutManager = layoutManagerHorizontal
        }

        rvNewsItems.apply {
            adapter = NewsAdapter(listener = object : NewsAdapter.NewsAdapterClickListener {
                override fun viewPicture(newsItem: NewsModel) {
                }

                override fun playVideo(newsItem: NewsModel) {
                }

            }, newsList)
            layoutManager = layoutManagerHorizontal1
            val dividerItemDecoration = DividerItemDecoration(
                rvNewsItems.context,
                layoutManagerHorizontal1.orientation
            )
            addItemDecoration(dividerItemDecoration)

        }

        rvPintsTable.apply {
            adapter = TablePointsAdapter(pointsList)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tvViewAllVideos -> {
                (activity as MainActivity).openFragment(
                    MediaFragment.newInstance()
                )
            }
            R.id.tvViewAllNews -> {
                (activity as MainActivity).openFragment(
                    NewsFragment.newInstance()
                )
            }
            R.id.tvViewFullTable -> {
                (activity as MainActivity)
                (activity as MainActivity).openFragment(
                    TableFragment.newInstance()
                )
            }
        }
    }

}
