package com.logicielhouse.ca.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
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

    interface MatchTicketClickListener {
        fun onMatchTicketClickListener()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRidesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.ticket_matches, parent, false)
        return MyRidesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyRidesViewHolder, position: Int) {
        val match: MatchesModel = list[holder.adapterPosition]
        holder.apply {
            tvTeam1Name?.text = match.team1Name
            tvTeam2Name?.text = match.team2Name

            ivTeam1Logo?.let {
                Glide.with(tvTeam1Name?.context as Activity).load(match.team1Logo)
                    .override(SIZE_ORIGINAL)
                    .error(R.drawable.abc_vector_test)
                    .into(it)
            }
            ivTeam2Logo?.let {
                Glide.with(tvTeam1Name?.context as Activity).load(match.team2Logo)
                    .override(SIZE_ORIGINAL)
                    .error(R.drawable.abc_vector_test)
                    .into(it)
            }
            if (match.result.isEmpty()) {
                tvDate?.text = convertDateTimeToMDY(match.timeSchedule)
                tvLocation?.text = match.match_description
                tvTime?.text = convertTimeTo12HrFormat(match.timeSchedule)
                tvName?.text = match.match_description
                resultBlock.visibility = View.GONE
                fixtureBlock.visibility = View.VISIBLE
            } else {
                tvDate?.text = convertDateTimeToMDY(match.timeSchedule)
                tvLocation?.text = match.match_description
                try {
                    val resultArray = match.result.split("-")

                    tvTeam1Score?.text = resultArray[0]
                    tvTeam2Score?.text = resultArray[1]
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                resultBlock.visibility = View.VISIBLE
                fixtureBlock.visibility = View.GONE

            }

            itemView.setOnClickListener {
                onMatchTicketClickListener.onMatchTicketClickListener()
            }
        }
    }

    override fun getItemCount(): Int = list.size

    class MyRidesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTeam1Name: TextView? = itemView.findViewById(R.id.tvTeam1Name)
        val tvTeam2Name: TextView? = itemView.findViewById(R.id.tvTeam2Name)
        val tvDate: TextView? = itemView.findViewById(R.id.tvDate)
        val tvLocation: TextView? = itemView.findViewById(R.id.tvLocation)
        val tvResultDate: TextView? = itemView.findViewById(R.id.tvResultDate)
        val tvResultLocation: TextView? = itemView.findViewById(R.id.tvResultLocation)
        val tvTeam1Score: TextView? = itemView.findViewById(R.id.tvTeam1Score)
        val tvTeam2Score: TextView? = itemView.findViewById(R.id.tvTeam2Score)
        val tvTime: TextView? = itemView.findViewById(R.id.tvTime)
        val tvName: TextView? = itemView.findViewById(R.id.tvName)
        val ivTeam1Logo: ImageView? = itemView.findViewById(R.id.ivTeam1Logo)
        val ivTeam2Logo: ImageView? = itemView.findViewById(R.id.ivTeam2Logo)
        val fixtureBlock: LinearLayout = itemView.findViewById(R.id.fixtureBlock)
        val resultBlock: RelativeLayout = itemView.findViewById(R.id.resultBlock)
    }


}