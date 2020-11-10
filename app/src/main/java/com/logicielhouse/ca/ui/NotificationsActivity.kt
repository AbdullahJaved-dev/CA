package com.logicielhouse.ca.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.logicielhouse.ca.R
import com.logicielhouse.ca.adapter.NotificationsAdapter
import com.logicielhouse.ca.model.NotificationModel
import com.logicielhouse.ca.utils.AppConstants
import com.logicielhouse.ca.utils.displayMessage
import com.logicielhouse.ca.utils.locale.LocaleManager
import kotlinx.android.synthetic.main.activity_notifications.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class NotificationsActivity : AppCompatActivity(), NotificationsAdapter.NotificationClickListener {

    private lateinit var listener: NotificationsAdapter.NotificationClickListener
    private var notificationsList = ArrayList<NotificationModel>()
    private lateinit var notificationsAdapter: NotificationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        supportActionBar?.title = getString(R.string.notifications)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listener = this

        setupUI()

    }

    private fun setupUI() {
        notificationsAdapter = NotificationsAdapter(listener, notificationsList)
        rvNotifications.apply {
            adapter = notificationsAdapter
            layoutManager =
                LinearLayoutManager(this@NotificationsActivity, RecyclerView.VERTICAL, false)
        }
    }

    override fun onStart() {
        super.onStart()
        loadNotifications()
    }

    private fun loadNotifications() {
        val notRequest = object :
            JsonObjectRequest(Method.GET, AppConstants.GET_NOTIFICATIONS, null, { response ->
                try {
                    notificationsList.clear()
                    val notArray: JSONArray = response.getJSONArray("notification")
                    var i = 0
                    while (i < notArray.length()) {
                        val notObject: JSONObject = notArray.getJSONObject(i)
                        val notificationModel = NotificationModel(
                            notObject.optString("notification_heading"),
                            notObject.optString("notification_body"),
                            notObject.optString("created_at"),
                        )
                        notificationsList.add(notificationModel)
                        i++
                    }
                    notificationsAdapter.notifyDataSetChanged()
                    if (notProgressBar != null) {
                        notProgressBar.visibility = View.GONE
                        if (notificationsList.size == 0) {
                            tvNoDataFound.visibility = View.VISIBLE
                        } else {
                            tvNoDataFound.visibility = View.GONE
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, { error ->
                try {
                    notProgressBar.visibility = View.GONE
                    val response: NetworkResponse = error.networkResponse
                    if (response.data != null) {
                        val errorObj = JSONObject(String(response.data))
                        displayMessage(this, errorObj.optString("message"))
                    }
                } catch (e: Exception) {
                    displayMessage(this, getString(R.string.unknown_error))
                }
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                // val pref = getSharedPreferences(BaseApplication.PREFS, Context.MODE_PRIVATE)
                //headers["languages-code"] = pref?.getString(BaseApplication.LOCALE, "ar") as String
                headers["Accept"] = "application/json"
                return headers
            }
        }
        notRequest.retryPolicy = DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        requestQueue.add(notRequest)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleManager.setLocale(newBase!!))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun openMedia(notificationModel: NotificationModel) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}