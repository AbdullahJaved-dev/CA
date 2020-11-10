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
import com.logicielhouse.ca.adapter.NewsAdapter
import com.logicielhouse.ca.model.NewsModel
import com.logicielhouse.ca.model.PicturesModel
import com.logicielhouse.ca.model.VideosModel
import com.logicielhouse.ca.utils.AppConstants
import com.logicielhouse.ca.utils.displayMessage
import com.logicielhouse.ca.utils.locale.LocaleManager
import kotlinx.android.synthetic.main.fragment_news.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class NewsActivity : AppCompatActivity(), NewsAdapter.NewsAdapterClickListener {
    private var newsList = ArrayList<NewsModel>()
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsAdapterClickListener: NewsAdapter.NewsAdapterClickListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        supportActionBar?.title = getString(R.string.news)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        newsAdapterClickListener = this
        setupUI()
        getAllNews()
        swipeRefresh.setOnRefreshListener {
            newsProgressBar.visibility = View.VISIBLE
            newsList = ArrayList()
            getAllNews()
            swipeRefresh.isRefreshing =
                false
        }
    }

    private fun setupUI() {
        newsAdapter = NewsAdapter(newsAdapterClickListener, newsList)
        rvNews.apply {
            adapter = newsAdapter
            layoutManager =
                LinearLayoutManager(
                    this@NewsActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
        }
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

    private fun getAllNews() {
        val newsRequest = object :
            JsonObjectRequest(Method.GET, AppConstants.GET_NEWS, null, { response ->
                try {
                    newsList.clear()
                    val newsArray: JSONArray = response.getJSONArray("news")
                    var i = 0
                    while (i < newsArray.length()) {
                        val newsObject: JSONObject = newsArray.getJSONObject(i)
                        val newsModel = NewsModel(
                            newsObject.getInt("news_id"),
                            newsObject.optString("imageUri"),
                            newsObject.getString("news_title"),
                            newsObject.optString("thumbnailUri"),
                            newsObject.optString("videoUri"),
                            newsObject.getString("created_at"),
                            newsObject.getString("categories_name"),
                            newsObject.getString("news_details")
                        )
                        newsList.add(newsModel)
                        Log.d("NewsArrayList", newsList.toString())
                        i++
                    }
                    Log.d("NewsArrayList", newsList.toString())
                    newsAdapter.notifyDataSetChanged()
                    if (newsProgressBar != null) {
                        newsProgressBar.visibility = View.GONE
                        if (newsList.size == 0) {
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
                    newsProgressBar.visibility = View.GONE
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
                val pref =
                    getSharedPreferences(BaseApplication.PREFS, Context.MODE_PRIVATE)
                headers["languages-code"] = pref?.getString(BaseApplication.LOCALE, "ar") as String
                return headers
            }
        }
        newsRequest.retryPolicy = DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
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
        val intent = Intent(this, ViewMediaActivity::class.java)
        intent.putExtra("picture", picturesModel)
        startActivity(intent)
    }

    private fun viewVideoMedia(videosModel: VideosModel) {
        val intent = Intent(this, ViewMediaActivity::class.java)
        intent.putExtra("video", videosModel)
        startActivity(intent)
    }
}