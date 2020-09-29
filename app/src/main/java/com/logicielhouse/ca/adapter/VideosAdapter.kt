package com.logicielhouse.ca.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.logicielhouse.ca.R
import com.logicielhouse.ca.model.VideosModel

/**
 * Created by Abdullah on 9/27/2020.
 */
class VideosAdapter(
    private val listener: VideosAdapterClickListeners,
    private val videosList: List<VideosModel>, private val type: String
) :
    RecyclerView.Adapter<VideosAdapter.VideosViewHolder>() {
    class VideosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvVideoTitle: TextView = itemView.findViewById(R.id.tvMediaTitle)
        val tvVideoDate: TextView = itemView.findViewById(R.id.tvMediaDate)
        val tvVideoSource: TextView = itemView.findViewById(R.id.tvMediaSource)
        val ivVideoThumbnail: ImageView = itemView.findViewById(R.id.ivMediaThumbnail)
        val ivPlayBtn: ImageView = itemView.findViewById(R.id.ivPlayBtn)
    }

    interface VideosAdapterClickListeners {
        fun playVideo(videoItem: VideosModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosViewHolder {
        val view = if (type != "home") {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_video_item, parent, false)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_home_video_item, parent, false)
        }

        return VideosViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideosViewHolder, position: Int) {
        val videoItem = videosList[position]
        setVideoData(holder, videoItem)
    }

    private fun setVideoData(holder: VideosViewHolder, videoItem: VideosModel) {
        holder.apply {
            tvVideoTitle.text = videoItem.videoTitle
            tvVideoDate.text = videoItem.videoDate
            tvVideoSource.text = videoItem.videoSource
            Glide.with(ivVideoThumbnail.context).load(videoItem.videoThumbnail)
                .into(ivVideoThumbnail)
            ivPlayBtn.setOnClickListener {
                listener.playVideo(videoItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return videosList.size
    }

}