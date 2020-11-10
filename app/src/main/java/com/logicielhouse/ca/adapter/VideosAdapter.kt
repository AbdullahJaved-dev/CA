package com.logicielhouse.ca.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.logicielhouse.ca.R
import com.logicielhouse.ca.model.VideosModel

/**
 * Created by Abdullah on 9/27/2020.
 */
class VideosAdapter(
    private val listener: VideosAdapterClickListeners,
    private val videosList: List<VideosModel>, private val type: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_VIDEO: Int = 1
        const val TYPE_AD: Int = 2
    }

    inner class AdViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val adView: AdView = view.findViewById(R.id.adView)
        fun setAdDetail() {
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return try {
            if (position > 0 && position % 5 == 0) {
                TYPE_AD
            } else {
                TYPE_VIDEO
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: View?
        return if (viewType == TYPE_VIDEO) {
            view = if (type != "home") {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_video_item, parent, false)
            } else {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_home_video_item, parent, false)
            }
            VideosViewHolder(view)

        } else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_ad_item, parent, false)
            AdViewHolder(view)
        }
    }


    inner class VideosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvVideoTitle: TextView = itemView.findViewById(R.id.tvMediaTitle)
        val tvVideoDate: TextView = itemView.findViewById(R.id.tvMediaDate)
        val tvVideoSource: TextView = itemView.findViewById(R.id.tvMediaSource)
        val viewBorder: View = itemView.findViewById(R.id.viewBorder)
        val ivVideoThumbnail: ImageView = itemView.findViewById(R.id.ivMediaThumbnail)
        val ivPlayBtn: ImageView = itemView.findViewById(R.id.ivPlayBtn)
        fun setVideoData(videoItem: VideosModel) {
            tvVideoTitle.text = videoItem.videoTitle
            tvVideoDate.text = videoItem.videoDate
            tvVideoSource.visibility = View.GONE
            viewBorder.visibility = View.GONE
            Glide.with(ivVideoThumbnail.context).load(videoItem.videoThumbnail)
                .into(ivVideoThumbnail)
            ivPlayBtn.setOnClickListener {
                listener.playVideo(videoItem)
            }
        }
    }

    interface VideosAdapterClickListeners {
        fun playVideo(videoItem: VideosModel)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when {
            getItemViewType(position) == TYPE_VIDEO -> {
                val videoItem = videosList[getOriginalPosition(position)]
                (holder as VideosAdapter.VideosViewHolder).setVideoData(videoItem)
            }
            else -> {
                (holder as VideosAdapter.AdViewHolder).setAdDetail()
            }
        }


    }

    override fun getItemCount(): Int {

        var additionalContent = 0
        if (videosList.isNotEmpty() && videosList.size > 5) {
            additionalContent = videosList.size / 5
        }

        return videosList.size + additionalContent
    }

}