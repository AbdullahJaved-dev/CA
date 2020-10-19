package com.logicielhouse.ca.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.logicielhouse.ca.R
import com.logicielhouse.ca.model.TablePointsModel

/**
 * Created by Abdullah on 9/27/2020.
 */
class TablePointsAdapter(private val pointsList: ArrayList<TablePointsModel>) :
    RecyclerView.Adapter<TablePointsAdapter.PointsViewHolder>() {
    class PointsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPosition: TextView = itemView.findViewById(R.id.tvPosition)
        val tvClubName: TextView = itemView.findViewById(R.id.tvClubName)
        val tvGamesPlayed: TextView = itemView.findViewById(R.id.tvGamesPlayed)
        val tvGoalDifference: TextView = itemView.findViewById(R.id.tvGoalDifference)
        val tvPoints: TextView = itemView.findViewById(R.id.tvPoints)
        val ivClubIcon: ImageView = itemView.findViewById(R.id.ivClubIcon)
        val ivRankStatus: ImageView = itemView.findViewById(R.id.ivPositionUpDown)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_table_points_item, parent, false)
        return PointsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PointsViewHolder, position: Int) {
        val pointsItem = pointsList[position]
        holder.apply {
            tvPosition.text = pointsItem.position.toString()
            tvClubName.text = pointsItem.clubName
            tvGamesPlayed.text = pointsItem.gamePlayed.toString()
            tvGoalDifference.text = pointsItem.goalDifference.toString()
            tvPoints.text = pointsItem.points.toString()
            Glide.with(ivClubIcon.context).load(pointsItem.clubImage).error(R.drawable.leicester)
                .into(ivClubIcon)
            when (pointsItem.rankStatus) {
                0 -> {
                    Glide.with(ivRankStatus.context).load(
                        ContextCompat.getDrawable(
                            ivRankStatus.context,
                            R.drawable.ic_same
                        )
                    ).into(ivRankStatus)
                }
                1 -> {
                    Glide.with(ivRankStatus.context).load(
                        ContextCompat.getDrawable(
                            ivRankStatus.context,
                            R.drawable.ic_up
                        )
                    ).into(ivRankStatus)
                }
                else -> {
                    Glide.with(ivRankStatus.context).load(
                        ContextCompat.getDrawable(
                            ivRankStatus.context,
                            R.drawable.ic_down
                        )
                    ).into(ivRankStatus)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return pointsList.size
    }

}