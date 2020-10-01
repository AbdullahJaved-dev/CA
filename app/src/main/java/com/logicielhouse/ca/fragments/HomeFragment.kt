package com.logicielhouse.ca.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.NetworkResponse
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.logicielhouse.ca.*
import com.logicielhouse.ca.adapter.NewsAdapter
import com.logicielhouse.ca.adapter.TablePointsAdapter
import com.logicielhouse.ca.adapter.VideosAdapter
import com.logicielhouse.ca.model.NewsModel
import com.logicielhouse.ca.model.PicturesModel
import com.logicielhouse.ca.model.TablePointsModel
import com.logicielhouse.ca.model.VideosModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class HomeFragment : Fragment(R.layout.fragment_home), View.OnClickListener,
    NewsAdapter.NewsAdapterClickListener,
    VideosAdapter.VideosAdapterClickListeners {
    private var newsList: ArrayList<NewsModel> = ArrayList()
    private var videosList: ArrayList<VideosModel> = ArrayList()
    private var pointsList: ArrayList<TablePointsModel> = ArrayList()
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var videosAdapter: VideosAdapter
    private lateinit var tablePointsAdapter: TablePointsAdapter

    private lateinit var newsAdapterClickListener: NewsAdapter.NewsAdapterClickListener
    private lateinit var videosAdapterClickListeners: VideosAdapter.VideosAdapterClickListeners


    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsAdapterClickListener = this
        videosAdapterClickListeners = this
        setupUI()
        setupClickListeners()
        getAllNews()
        getAllVideos()
        getTablePoints()
    }

    private fun setupClickListeners() {
        tvViewAllVideos.setOnClickListener(this)
        tvViewAllNews.setOnClickListener(this)
        tvViewFullTable.setOnClickListener(this)
    }

    private fun setupUI() {
        (activity as MainActivity).bottomNavigation.menu.getItem(2).isChecked = true
        (activity as MainActivity).setToolbarTitle(getString(R.string.home))
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tvViewAllVideos -> {
                (activity as MainActivity).openFragment(
                    MediaFragment.newInstance()
                )
            }
            R.id.tvViewAllNews -> {
                (activity as MainActivity).openFragment(
                    NewsFragment.newInstance()
                )
            }
            R.id.tvViewFullTable -> {
                (activity as MainActivity)
                (activity as MainActivity).openFragment(
                    TableFragment.newInstance()
                )
            }
        }
    }

    private fun getAllNews() {
        val newsRequest = object :
            JsonObjectRequest(Method.GET, AppConstants.GET_NEWS, null, { response ->
                try {
                    newsList.clear()
                    val newsArray: JSONArray = response.getJSONArray("news")
                    var i = 0
                    while (i < newsArray.length() && i < 4) {
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
                    val layoutManagerHorizontal1 =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                    rvNewsItems.apply {
                        adapter = newsAdapter
                        layoutManager = layoutManagerHorizontal1
                        val dividerItemDecoration = DividerItemDecoration(
                            rvNewsItems.context,
                            layoutManagerHorizontal1.orientation
                        )
                        addItemDecoration(dividerItemDecoration)
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

    private fun getAllVideos() {
        val videosRequest = object :
            JsonObjectRequest(Method.GET, AppConstants.GET_SOURCES, null, { response ->
                try {
                    videosList.clear()
                    val videosArray: JSONArray = response.getJSONArray("sources")
                    var i = 0
                    while (i < videosArray.length() && i < 4) {
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
                    Log.d("videosArrayList", videosList.toString())
                    videosAdapter = VideosAdapter(videosAdapterClickListeners, videosList, "home")
                    val layoutManagerHorizontal =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                    rvHomeVideos.apply {
                        adapter = videosAdapter
                        layoutManager = layoutManagerHorizontal
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

    private fun getTablePoints() {
        val pointsRequest = object :
            JsonObjectRequest(Method.GET, AppConstants.GET_POINTS, null, { response ->
                try {
                    pointsList.clear()
                    val pointsArray: JSONArray = response.getJSONArray("club_point")
                    var i = 0
                    while (i < pointsArray.length() && i < 4) {
                        val pointsObject: JSONObject = pointsArray.getJSONObject(i)
                        val pointsModel = TablePointsModel(
                            pointsObject.optInt("club_id"),
                            pointsObject.optInt("position"),
                            pointsObject.getString("club_logo_uri"),
                            pointsObject.getString("club_title"),
                            pointsObject.optInt("gamePlayed"),
                            pointsObject.optInt("goalDifference"),
                            pointsObject.optInt("Points"),
                            pointsObject.optInt("rankStatus")
                        )
                        pointsList.add(pointsModel)
                        i++
                    }
                    Log.d("pointsArrayList", pointsList.toString())
                    tablePointsAdapter = TablePointsAdapter(pointsList)

                    rvPintsTable.apply {
                        adapter = tablePointsAdapter
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
                headers["languages-code"] = pref?.getString(BaseApplication.LOCALE, "en") as String
                return headers
            }
        }
        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(pointsRequest)
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

    override fun playVideo(videoItem: VideosModel) {
        viewVideoMedia(videoItem)
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
