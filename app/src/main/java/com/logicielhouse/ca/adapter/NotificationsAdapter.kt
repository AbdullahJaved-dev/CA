package com.logicielhouse.ca.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.logicielhouse.ca.R
import com.logicielhouse.ca.model.NotificationModel
import com.logicielhouse.ca.utils.convertDate

/**
 * Created by Abdullah on 11/10/2020.
 */
class NotificationsAdapter(
    private val listener: NotificationClickListener,
    private val notificationsList: ArrayList<NotificationModel>
) :
    RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>() {
    class NotificationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvNotificationTitle)
        val date: TextView = itemView.findViewById(R.id.tvNotificationDate)
        val desc: TextView = itemView.findViewById(R.id.tvNotificationDescription)
    }

    interface NotificationClickListener {
        fun openMedia(notificationModel: NotificationModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_notification_item, parent, false)
        return NotificationsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        val notificationModel = notificationsList[position]
        holder.apply {
            title.text = notificationModel.title
            desc.text = notificationModel.message
            date.text = convertDate(notificationModel.date)

            itemView.setOnClickListener {
                listener.openMedia(notificationModel)
            }
        }
    }

    override fun getItemCount(): Int {
        return notificationsList.size
    }
}