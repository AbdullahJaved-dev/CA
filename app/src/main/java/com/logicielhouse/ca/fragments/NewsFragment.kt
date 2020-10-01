package com.logicielhouse.ca.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.logicielhouse.ca.*
import com.logicielhouse.ca.adapter.NewsAdapter
import com.logicielhouse.ca.model.NewsModel
import com.logicielhouse.ca.model.PicturesModel
import com.logicielhouse.ca.model.VideosModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_news.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class NewsFragment : Fragment(R.layout.fragment_news), NewsAdapter.NewsAdapterClickListener {
    private var newsList = ArrayList<NewsModel>()
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsAdapterClickListener: NewsAdapter.NewsAdapterClickListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).bottomNavigation.menu.getItem(1).isChecked = true
        (activity as MainActivity).setToolbarTitle(getString(R.string.news))
        newsAdapterClickListener = this
        getAllNews()
        swipeRefresh.setOnRefreshListener {
            newsList = ArrayList()
            getAllNews()
            swipeRefresh.isRefreshing =
                false
        }
    }

    companion object {
        fun newInstance() = NewsFragment()
    }

    private fun getAllNews() {
        val newsRequest = object :
            JsonObjectRequest(Request.Method.GET, AppConstants.GET_NEWS, null, { response ->
                try {
                    newsList.clear()
                    val newsArray: JSONArray = response.getJSONArray("news")
                    var i = 0
                    while (i < newsArray.length()) {
                        val newsObject: JSONObject = newsArray.getJSONObject(i)
                        val newsModel = NewsModel(
                            newsObject.getInt("news_id"),
                            newsObject.getString("imageUri"),
                            newsObject.getString("news_title"),
                            newsObject.getString("created_at"),
                            newsObject.getString("news_details"),
                            newsObject.getString("categories_name"),
                            newsObject.getString("thumbnailUri"),
                            newsObject.getString("videoUri"),
                        )
                        newsList.add(newsModel)
                        Log.d("NewsArrayList", newsList.toString())
                        i++
                    }
                    Log.d("NewsArrayList", newsList.toString())

                    newsAdapter = NewsAdapter(newsAdapterClickListener, newsList)
                    try {
                        rvNews.apply {
                            adapter = newsAdapter
                            layoutManager =
                                LinearLayoutManager(
                                    requireContext(),
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                        }
                    } catch (e1: Exception) {
                        newsList.clear()
                        getAllNews()
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
                headers["languages-code"] = pref?.getString(BaseApplication.LOCALE, "en") as String
                return headers
            }
        }
        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(newsRequest)
    }

    override fun viewPicture(newsItem: NewsModel) {
        val picturesModel = PicturesModel(
            newsItem.id,
            newsItem.pictureURI,
            newsItem.title,
            newsItem.date,
            newsItem.description
        )
        viewPictureMedia(picturesModel)
    }

    override fun playVideo(newsItem: NewsModel) {
        val videosModel = VideosModel(
            newsItem.id,
            newsItem.videoThumbnail,
            newsItem.videoURI,
            newsItem.title,
            newsItem.date,
            newsItem.description
        )
        viewVideoMedia(videosModel)
    }

    private fun viewPictureMedia(picturesModel: PicturesModel) {
        val intent = Intent(requireContext(), ViewMediaActivity::class.java)
        intent.putExtra("picture", picturesModel)
        startActivity(intent)
    }

    private fun viewVideoMedia(videosModel: VideosModel) {
        val intent = Intent(requireContext(), ViewMediaActivity::class.java)
        intent.putExtra("video", videosModel)
        startActivity(intent)
    }
}
