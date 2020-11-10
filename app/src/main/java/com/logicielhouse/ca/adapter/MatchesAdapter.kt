package com.logicielhouse.ca.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.logicielhouse.ca.R
import com.logicielhouse.ca.model.MatchesModel
import com.logicielhouse.ca.utils.convertDateTimeToMDY
import com.logicielhouse.ca.utils.convertTimeTo12HrFormat

/**
 * Created by Abdullah on 10/23/2020.
 */
class MatchesAdapters(
    private val list: List<MatchesModel>,
    private val onMatchTicketClickListener: MatchTicketClickListener
) : RecyclerView.Adapter<MatchesAdapters.MyRidesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRidesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyRidesViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyRidesViewHolder, position: Int) {
        val match: MatchesModel = list[position]
        holder.bind(match, onMatchTicketClickListener)
    }

    override fun getItemCount(): Int = list.size

    inner class MyRidesViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.ticket_matches, parent, false)) {
        private var tvTeam1Name: TextView? = null
        private var tvTeam2Name: TextView? = null
        private var tvDate: TextView? = null
        private var tvLocation: TextView? = null
        private var tvTime: TextView? = null
        private var tvName: TextView? = null
        private var ivTeam1Logo: ImageView? = null
        private var ivTeam2Logo: ImageView? = null


        init {
            tvTeam1Name = itemView.findViewById(R.id.tvTeam1Name)
            tvTeam2Name = itemView.findViewById(R.id.tvTeam2Name)
            tvDate = itemView.findViewById(R.id.tvDate)
            tvLocation = itemView.findViewById(R.id.tvLocation)
            tvTime = itemView.findViewById(R.id.tvTime)
            tvName = itemView.findViewById(R.id.tvName)
            ivTeam1Logo = itemView.findViewById(R.id.ivTeam1Logo)
            ivTeam2Logo = itemView.findViewById(R.id.ivTeam2Logo)

        }

        fun bind(match: MatchesModel, onMatchTicketClickListener: MatchTicketClickListener) {
            tvTeam1Name?.text = match.team1Name
            tvTeam2Name?.text = match.team2Name
            tvDate?.text = convertDateTimeToMDY(match.timeSchedule)
            tvLocation?.text = match.match_description
            tvTime?.text = convertTimeTo12HrFormat(match.timeSchedule)
            tvName?.text = match.match_description

            ivTeam1Logo?.let {
                Glide.with(tvTeam1Name?.context as Activity).load(match.team1Logo)
                    .override(SIZE_ORIGINAL)
                    .into(it)
            }
            ivTeam2Logo?.let {
                Glide.with(tvTeam1Name?.context as Activity).load(match.team2Logo)
                    .override(SIZE_ORIGINAL)
                    .into(it)
            }

            itemView.setOnClickListener {
                onMatchTicketClickListener.onMatchTicketClickListener()
            }
        }

    }

    interface MatchTicketClickListener {
        fun onMatchTicketClickListener()
    }

}