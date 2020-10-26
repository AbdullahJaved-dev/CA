package com.logicielhouse.ca.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.logicielhouse.ca.BaseApplication
import com.logicielhouse.ca.R
import com.logicielhouse.ca.adapter.VideosAdapter
import com.logicielhouse.ca.model.VideosModel
import com.logicielhouse.ca.utils.AppConstants
import com.logicielhouse.ca.utils.displayMessage
import com.logicielhouse.ca.utils.locale.LocaleManager
import kotlinx.android.synthetic.main.fragment_videos.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class VideosActivity : AppCompatActivity(),
    VideosAdapter.VideosAdapterClickListeners {
    private lateinit var videosAdapterClickListeners: VideosAdapter.VideosAdapterClickListeners
    private var videosList = ArrayList<VideosModel>()
    private lateinit var videosAdapter: VideosAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videos)


        supportActionBar?.title = getString(R.string.videos)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        videosAdapterClickListeners = this

        videosAdapter = VideosAdapter(videosAdapterClickListeners, videosList, "video")
        rvVideos.apply {
            adapter = videosAdapter
            layoutManager =
                LinearLayoutManager(
                    this@VideosActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
        }

        getAllVideos()
        swipeRefresh.setOnRefreshListener {
            videosProgressBar.visibility = View.VISIBLE
            videosList = ArrayList()
            getAllVideos()
            swipeRefresh.isRefreshing =
                false
        }

    }

    private fun getAllVideos() {
        val videosRequest = object :
            JsonObjectRequest(Method.GET, AppConstants.GET_SOURCES, null, { response ->
                try {
                    videosList.clear()
                    val videosArray: JSONArray = response.getJSONArray("sources")
                    var i = 0
                    while (i < videosArray.length()) {
                        val videosObject: JSONObject = videosArray.getJSONObject(i)
                        val videosModel = VideosModel(
                            videosObject.optInt("source_id"),
                            videosObject.getString("thumbnailUri"),
                            videosObject.getString("source_link"),
                            videosObject.optString("title"),
                            videosObject.optString("created_at"),
                            videosObject.optString("details")
                        )
                        videosList.add(videosModel)
                        i++
                    }
                    videosAdapter.notifyDataSetChanged()
                    if (videosProgressBar != null) {
                        videosProgressBar.visibility = View.GONE
                        if (videosList.size == 0) {
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
                    videosProgressBar.visibility = View.GONE
                    val response: NetworkResponse = error.networkResponse
                    if (response.data != null) {
                        val errorObj = JSONObject(String(response.data))
                        displayMessage(this, errorObj.optString("message"))
                    }
                } catch (e: Exception) {
                    Log.e("TAG", "getAllVideos: " + e.localizedMessage)
                    displayMessage(this, getString(R.string.unknown_error))
                }
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                val pref =
                    getSharedPreferences(BaseApplication.PREFS, Context.MODE_PRIVATE)
                headers["type"] = "video"
                headers["languages-code"] = pref.getString(BaseApplication.LOCALE, "ar") as String
                return headers
            }

        }
        videosRequest.retryPolicy =
            DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        requestQueue.add(videosRequest)
    }

    override fun playVideo(videoItem: VideosModel) {
        val intent = Intent(this, ViewMediaActivity::class.java)
        intent.putExtra("video", videoItem)
        startActivity(intent)
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
}