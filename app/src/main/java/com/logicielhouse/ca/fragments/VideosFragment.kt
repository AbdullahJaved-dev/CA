package com.logicielhouse.ca.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.logicielhouse.ca.R
import com.logicielhouse.ca.adapter.VideosAdapter
import com.logicielhouse.ca.model.VideosModel
import kotlinx.android.synthetic.main.fragment_videos.*

class VideosFragment : Fragment(R.layout.fragment_videos) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvVideos.apply {
            adapter = VideosAdapter(listener = object : VideosAdapter.VideosAdapterClickListeners {
                override fun playVideo(videoItem: VideosModel) {

                }

            }, VideosModel.videosList,"videos")
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }
}