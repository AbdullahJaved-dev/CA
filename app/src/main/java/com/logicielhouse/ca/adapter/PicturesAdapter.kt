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
import com.logicielhouse.ca.model.PicturesModel

/**
 * Created by Abdullah on 9/27/2020.
 */
class PicturesAdapter(
    private val listener: PicturesAdapterClickListeners,
    private val picturesList: List<PicturesModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_PHOTO: Int = 1
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
                TYPE_PHOTO
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
        return if (viewType == TYPE_PHOTO) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_photos_item, parent, false)
            PicturesViewHolder(view)
        } else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_ad_item, parent, false)
            AdViewHolder(view)
        }
    }

    inner class PicturesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvPictureTitle: TextView = itemView.findViewById(R.id.tvPictureTitle)
        private val tvPictureDate: TextView = itemView.findViewById(R.id.tvPictureDate)
        private val ivPicture: ImageView = itemView.findViewById(R.id.ivPicture)
        private val tvPictureSource: TextView = itemView.findViewById(R.id.tvPictureSource)
        private val viewBorder: View = itemView.findViewById(R.id.viewBorder)

        fun setPictureDetails(pictureItem: PicturesModel) {
            tvPictureTitle.text = pictureItem.pictureTitle
            tvPictureDate.text = pictureItem.pictureDate
            tvPictureSource.visibility = View.GONE
            viewBorder.visibility = View.GONE
            Glide.with(ivPicture.context).load(pictureItem.pictureURI)
                .into(ivPicture)
            itemView.setOnClickListener {
                listener.viewPicture(pictureItem)
            }
        }
    }

    interface PicturesAdapterClickListeners {
        fun viewPicture(pictureItem: PicturesModel)
    }

    override fun getItemCount(): Int {
        var additionalContent = 0
        if (picturesList.isNotEmpty() && picturesList.size > 5) {
            additionalContent = picturesList.size / 5
        }
        return picturesList.size + additionalContent
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            getItemViewType(position) == TYPE_PHOTO -> {
                val pictureItem = picturesList[getOriginalPosition(position)]
                (holder as PicturesAdapter.PicturesViewHolder).setPictureDetails(pictureItem)
            }
            else -> {
                (holder as PicturesAdapter.AdViewHolder).setAdDetail()
            }
        }
    }

}