package com.logicielhouse.ca.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.logicielhouse.ca.R
import com.logicielhouse.ca.model.PicturesModel

/**
 * Created by Abdullah on 9/27/2020.
 */
class PicturesAdapter(
    private val listener: PicturesAdapterClickListeners,
    private val picturesList: List<PicturesModel>
) :
    RecyclerView.Adapter<PicturesAdapter.PicturesViewHolder>() {
    class PicturesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPictureTitle: TextView = itemView.findViewById(R.id.tvPictureTitle)
        val tvPictureDate: TextView = itemView.findViewById(R.id.tvPictureDate)
        val ivPicture: ImageView = itemView.findViewById(R.id.ivPicture)
        val tvPictureSource: TextView = itemView.findViewById(R.id.tvPictureSource)
        val viewBorder: View = itemView.findViewById(R.id.viewBorder)
    }

    interface PicturesAdapterClickListeners {
        fun viewPicture(pictureItem: PicturesModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicturesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_photos_item, parent, false)
        return PicturesViewHolder(view)
    }

    override fun onBindViewHolder(holder: PicturesViewHolder, position: Int) {
        val pictureItem = picturesList[position]
        holder.apply {
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

    override fun getItemCount(): Int {
        return picturesList.size
    }

}