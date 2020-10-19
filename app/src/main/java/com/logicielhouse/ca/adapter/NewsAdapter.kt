package com.logicielhouse.ca.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.logicielhouse.ca.R
import com.logicielhouse.ca.model.NewsModel

/**
 * Created by Abdullah on 9/27/2020.
 */
class NewsAdapter(
    private val listener: NewsAdapterClickListener,
    private val newsList: ArrayList<NewsModel>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_PHOTO: Int = 1
        const val TYPE_VIDEO: Int = 2
    }

    inner class VideoViewHolder(pictureParent: View) :
        RecyclerView.ViewHolder(pictureParent) {

        private val tvVideoTitle: TextView = pictureParent.findViewById(R.id.tvMediaTitle)
        private val tvVideoDate: TextView = pictureParent.findViewById(R.id.tvMediaDate)
        private val tvVideoSource: TextView = pictureParent.findViewById(R.id.tvMediaSource)
        private val ivVideoThumbnail: ImageView = pictureParent.findViewById(R.id.ivMediaThumbnail)
        private val ivPlayBtn: ImageView = pictureParent.findViewById(R.id.ivPlayBtn)
        fun setVideoDetails(newsItem: NewsModel) {
            tvVideoTitle.text = newsItem.title
            tvVideoDate.text = newsItem.date
            tvVideoSource.text = newsItem.videoSource
            Glide.with(ivVideoThumbnail.context).load(newsItem.videoThumbnail)
                .into(ivVideoThumbnail)
            ivPlayBtn.setOnClickListener {
                listener.playVideo(newsItem)
            }
        }
    }

    inner class PictureViewHolder(videoParent: View) :
        RecyclerView.ViewHolder(videoParent) {
        private val tvPictureTitle: TextView = videoParent.findViewById(R.id.tvPictureTitle)
        private val tvPictureDate: TextView = videoParent.findViewById(R.id.tvPictureDate)
        private val tvPictureSource: TextView = videoParent.findViewById(R.id.tvPictureSource)
        private val ivPicture: ImageView = videoParent.findViewById(R.id.ivPicture)
        fun setPictureDetails(newsItem: NewsModel) {
            tvPictureTitle.text = newsItem.title
            tvPictureDate.text = newsItem.date
            Glide.with(ivPicture.context).load(newsItem.pictureURI)
                .into(ivPicture)
            itemView.setOnClickListener {
                listener.viewPicture(newsItem)
            }
            tvPictureSource.text = newsItem.videoSource
        }
    }


    interface NewsAdapterClickListener {
        fun viewPicture(newsItem: NewsModel)
        fun playVideo(newsItem: NewsModel)
    }

    override fun getItemViewType(position: Int): Int {
        return if (newsList[position].videoURI == "null") {
            TYPE_PHOTO
        } else {
            Log.d("VideoDataLink", " ${newsList[position].videoURI}, $position")
            TYPE_VIDEO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return if (viewType == TYPE_PHOTO) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_photos_item, parent, false)
            PictureViewHolder(view)
        } else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_video_item, parent, false)
            VideoViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val newsModel = newsList[position]
        if (getItemViewType(position) == TYPE_VIDEO) {
            (holder as VideoViewHolder).setVideoDetails(newsModel)
        } else {
            (holder as PictureViewHolder).setPictureDetails(newsModel)
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }
}