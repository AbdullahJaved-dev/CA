package com.logicielhouse.ca.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.logicielhouse.ca.R
import com.logicielhouse.ca.model.NewsModel


/**
 * Created by Abdullah on 9/27/2020.
 */
class NewsAdapter(
    private val listener: NewsAdapterClickListener,
    private val newsList: ArrayList<NewsModel>
) :
    RecyclerView.Adapter<ViewHolder>() {
    companion object {
        const val TYPE_PHOTO: Int = 1
        const val TYPE_VIDEO: Int = 2
        const val TYPE_AD: Int = 3
    }

    inner class VideoViewHolder(pictureParent: View) :
        ViewHolder(pictureParent) {

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
        ViewHolder(videoParent) {
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

    inner class AdViewHolder(view: View) : ViewHolder(view) {
        private val adView: AdView = view.findViewById(R.id.adView)
        fun setAdDetail() {
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        }
    }


    interface NewsAdapterClickListener {
        fun viewPicture(newsItem: NewsModel)
        fun playVideo(newsItem: NewsModel)
    }

    override fun getItemViewType(position: Int): Int {
        return try {
            if (position > 0 && position % 5 == 0) {
                TYPE_AD
            } else {
                if (newsList[getOriginalPosition(position)].videoURI == "null") {
                    TYPE_PHOTO
                } else {
                    Log.d(
                        "VideoDataLink",
                        " ${newsList[getOriginalPosition(position)].videoURI}, ${
                            getOriginalPosition(position)
                        }"
                    )
                    TYPE_VIDEO
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    private fun getOriginalPosition(position: Int): Int {
        return if (position > 4) {
            position - (position / 5)
        } else {
            position
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View
        return when (viewType) {
            TYPE_PHOTO -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_photos_item, parent, false)
                PictureViewHolder(view)
            }
            TYPE_AD -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_ad_item, parent, false)
                return AdViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_video_item, parent, false)
                VideoViewHolder(view)
            }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val newsModel = newsList[getOriginalPosition(position)]
        when {
            getItemViewType(position) == TYPE_VIDEO -> {
                (holder as VideoViewHolder).setVideoDetails(newsModel)
            }
            getItemViewType(position) == TYPE_PHOTO -> {
                (holder as PictureViewHolder).setPictureDetails(newsModel)
            }
            else -> {
                (holder as AdViewHolder).setAdDetail()
            }
        }
    }

    override fun getItemCount(): Int {

        var additionalContent = 0
        if (newsList.size > 0 && newsList.size > 5) {
            additionalContent = newsList.size / 5
        }

        return newsList.size + additionalContent
    }
}