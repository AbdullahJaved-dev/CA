package com.logicielhouse.ca.service

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.logicielhouse.ca.model.NotificationModel
import com.logicielhouse.ca.ui.MainActivity
import com.logicielhouse.ca.utils.AppConstants
import com.logicielhouse.ca.utils.NotificationUtils
import com.logicielhouse.ca.utils.SessionManager
import org.json.JSONObject

/**
 * Created by Abdullah on 11/3/2020.
 */
class CAFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(
            "New Token",
            "Refreshed token: $token"
        )
        if (!TextUtils.isEmpty(token.trim())) {
            SessionManager.getInstance(this)?.setStringPref(AppConstants.FIREBASE_TOKEN, token)
            uploadToken(token, applicationContext)
        }
    }

    companion object {
        fun uploadToken(token: String, applicationContext: Context) {
            val requestQueue: RequestQueue = Volley.newRequestQueue(applicationContext)
            val jsonObj = JSONObject()
            jsonObj.put("device_id", token)
            val newsRequest = object :
                JsonObjectRequest(Method.POST, AppConstants.UPLOAD_TOKEN, jsonObj, {
                    Log.d("TAG", "uploadToken: Token Uploaded")
                    SessionManager.getInstance(applicationContext)
                        ?.setBooleanPref(AppConstants.FIREBASE_TOKEN_UPLOADED, true)
                }, {
                    Log.d("TAG", "uploadToken: Token Upload Failed")
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }
            }
            newsRequest.retryPolicy =
                DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            requestQueue.add(newsRequest)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(
            "NewMessage",
            "New Notification Data" + remoteMessage.from
        )
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(
                "NewMessage",
                "Message data payload: " + remoteMessage.data
            )
            val data: Map<String, String> = remoteMessage.data
            handleData(data, remoteMessage.notification!!)
        } else if (remoteMessage.notification != null) {
            Log.d(
                "NewMessage",
                "Message Notification Body: " + remoteMessage.notification!!.body
            )
            handleNotification(remoteMessage.notification!!)
        }
    }

    private fun handleNotification(remoteMessageNotification: RemoteMessage.Notification) {
        val message = remoteMessageNotification.body
        val title = remoteMessageNotification.title
        val notificationVO = NotificationModel()
        notificationVO.title = title!!
        notificationVO.message = message!!
        val resultIntent = Intent(applicationContext, MainActivity::class.java)
        val notificationUtils = NotificationUtils(applicationContext)
        notificationUtils.displayNotification(notificationVO, resultIntent)
        notificationUtils.playNotificationSound()
    }

    private fun handleData(
        data: Map<String, String>,
        remoteMsgNotification: RemoteMessage.Notification
    ) {
        val title = remoteMsgNotification.title
        val message = remoteMsgNotification.body
        val resultIntent = Intent(applicationContext, MainActivity::class.java)

        val notificationVO = NotificationModel()
        notificationVO.title = title!!
        notificationVO.message = message!!
        val notificationUtils = NotificationUtils(applicationContext)
        notificationUtils.displayNotification(notificationVO, resultIntent)
        notificationUtils.playNotificationSound()
    }
}