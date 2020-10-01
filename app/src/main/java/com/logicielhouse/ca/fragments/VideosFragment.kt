package com.logicielhouse.ca.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.NetworkResponse
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.logicielhouse.ca.*
import com.logicielhouse.ca.adapter.VideosAdapter
import com.logicielhouse.ca.model.VideosModel
import kotlinx.android.synthetic.main.fragment_videos.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class VideosFragment : Fragment(R.layout.fragment_videos),
    VideosAdapter.VideosAdapterClickListeners {
    private lateinit var videosAdapterClickListeners: VideosAdapter.VideosAdapterClickListeners
    private var videosList = ArrayList<VideosModel>()
    private lateinit var videosAdapter: VideosAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videosAdapterClickListeners = this

        getAllVideos()
        swipeRefresh.setOnRefreshListener {
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
                    Log.d("PhotosArrayList", videosList.toString())
                    videosAdapter = VideosAdapter(videosAdapterClickListeners, videosList, "video")
                    rvVideos.apply {
                        adapter = videosAdapter
                        layoutManager =
                            LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, { error ->
                val response: NetworkResponse = error.networkResponse
                if (response.data != null) {
                    val errorObj = JSONObject(String(response.data))
                    displayMessage(requireContext(), errorObj.optString("message"))
                }
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                val pref =
                    activity?.getSharedPreferences(BaseApplication.PREFS, Context.MODE_PRIVATE)
                headers["type"] = "video"
                headers["languages-code"] = pref?.getString(BaseApplication.LOCALE, "en") as String
                return headers
            }
        }
        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(videosRequest)
    }

    override fun playVideo(videoItem: VideosModel) {
        val intent = Intent(requireContext(), ViewMediaActivity::class.java)
        intent.putExtra("video", videoItem)
        startActivity(intent)
    }
}