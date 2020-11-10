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
import com.logicielhouse.ca.model.SongModel

/**
 * Created by Abdullah on 10/23/2020.
 */
class SongsAdapter(
    private val listener: SongsAdapterClickListeners,
    private val songsList: List<SongModel>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_SONG: Int = 1
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
                TYPE_SONG
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

    inner class SongsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val tvSongTitle: TextView = itemView.findViewById(R.id.tvSongTitle)
        val ivSongPicture: ImageView = itemView.findViewById(R.id.ivSongPicture)

        fun setSongDetails(songModel: SongModel) {
            tvSongTitle.text = songModel.songTitle
            Glide.with(ivSongPicture.context).load(songModel.songThumbnail)
                .into(ivSongPicture)
            itemView.setOnClickListener {
                listener.playSong(songModel)
            }
        }
    }

    interface SongsAdapterClickListeners {
        fun playSong(songModel: SongModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: View?
        return if (viewType == TYPE_SONG) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_song_item, parent, false)
            SongsViewHolder(view)
        } else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_ad_item, parent, false)
            AdViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when {
            getItemViewType(position) == TYPE_SONG -> {
                val songModel = songsList[getOriginalPosition(position)]
                (holder as SongsAdapter.SongsViewHolder).setSongDetails(songModel)
            }
            else -> {
                (holder as SongsAdapter.AdViewHolder).setAdDetail()
            }
        }
    }

    override fun getItemCount(): Int {
        var additionalContent = 0
        if (songsList.isNotEmpty() && songsList.size > 5) {
            additionalContent = songsList.size / 5
        }
        return songsList.size + additionalContent
    }

}