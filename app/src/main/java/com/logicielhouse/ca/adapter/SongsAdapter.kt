package com.logicielhouse.ca.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.logicielhouse.ca.R
import com.logicielhouse.ca.model.SongModel

/**
 * Created by Abdullah on 10/23/2020.
 */
class SongsAdapter(
    private val listener: SongsAdapterClickListeners,
    private val songsList: List<SongModel>
) :
    RecyclerView.Adapter<SongsAdapter.SongsViewHolder>() {
    class SongsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSongTitle: TextView = itemView.findViewById(R.id.tvSongTitle)
        val ivSongPicture: ImageView = itemView.findViewById(R.id.ivSongPicture)
    }

    interface SongsAdapterClickListeners {
        fun playSong(songModel: SongModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_song_item, parent, false)
        return SongsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        val songModel = songsList[position]
        holder.apply {
            tvSongTitle.text = songModel.songTitle

            Glide.with(ivSongPicture.context).load(songModel.songThumbnail)
                .into(ivSongPicture)
            itemView.setOnClickListener {
                listener.playSong(songModel)
            }
        }
    }

    override fun getItemCount(): Int {
        return songsList.size
    }

}