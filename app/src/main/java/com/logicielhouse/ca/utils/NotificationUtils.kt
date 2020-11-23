package com.logicielhouse.ca.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.logicielhouse.ca.R
import com.logicielhouse.ca.model.NotificationModel
import com.logicielhouse.ca.ui.MainActivity
import com.logicielhouse.ca.utils.AppConstants.Companion.CHANNEL_ID
import com.logicielhouse.ca.utils.AppConstants.Companion.CHANNEL_NAME
import java.util.*

/**
 * Created by Abdullah on 11/3/2020.
 */
class NotificationUtils(private val mContext: Context) {

    private var activityMap = HashMap<String, Class<*>>()
    private val m = (Date().time / 1000L % Int.MAX_VALUE).toInt()

    init {
        //Populate activity map
        activityMap["MainActivity"] = MainActivity::class.java
    }

    fun displayNotification(notificationModel: NotificationModel, resultIntent: Intent) {
        val message: String = notificationModel.message
        val title: String = notificationModel.title
        resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        val resultPendingIntent: PendingIntent = PendingIntent.getActivity(
            mContext,
            m,
            resultIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val notificationManager =
            mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Notification from CA"
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }
        val mBuilder = NotificationCompat.Builder(
            mContext, CHANNEL_ID
        )
        mBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.logo)
            .setTicker("ZAT Worker")
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setContentInfo("info")
            .setContentIntent(resultPendingIntent)
            .setVibrate(longArrayOf(0, 1000, 500, 1000))
            .setLights(Color.argb(0, 255, 0, 0), 200, 100)
        notificationManager.notify(m, mBuilder.build())
    }

    fun playNotificationSound() {
        try {
            val alarmSound: Uri =
                Uri.parse("android.resource://" + mContext.packageName + "/" + R.raw.notification)
            val r: Ringtone = RingtoneManager.getRingtone(mContext, alarmSound)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}