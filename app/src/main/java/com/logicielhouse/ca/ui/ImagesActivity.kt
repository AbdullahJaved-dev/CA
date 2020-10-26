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
import com.logicielhouse.ca.adapter.PicturesAdapter
import com.logicielhouse.ca.model.PicturesModel
import com.logicielhouse.ca.utils.AppConstants
import com.logicielhouse.ca.utils.displayMessage
import com.logicielhouse.ca.utils.locale.LocaleManager
import kotlinx.android.synthetic.main.fragment_pivtures.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ImagesActivity : AppCompatActivity(),
    PicturesAdapter.PicturesAdapterClickListeners {
    private lateinit var picturesAdapterClickListeners: PicturesAdapter.PicturesAdapterClickListeners
    private var photosList = ArrayList<PicturesModel>()
    private lateinit var picturesAdapter: PicturesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)


        supportActionBar?.title = getString(R.string.photos)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        picturesAdapterClickListeners = this
        setupUI()
        getAllPhotos()
        swipeRefresh.setOnRefreshListener {
            photosProgressBar?.visibility = View.VISIBLE
            photosList = ArrayList()
            getAllPhotos()
            swipeRefresh.isRefreshing =
                false
        }
    }

    private fun setupUI() {
        picturesAdapter = PicturesAdapter(picturesAdapterClickListeners, photosList)
        rvPictures.apply {
            adapter = picturesAdapter
            layoutManager =
                LinearLayoutManager(
                    this@ImagesActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
        }
    }

    override fun viewPicture(pictureItem: PicturesModel) {
        val intent = Intent(this, ViewMediaActivity::class.java)
        intent.putExtra("picture", pictureItem)
        startActivity(intent)
    }

    private fun getAllPhotos() {
        val photosRequest = object :
            JsonObjectRequest(Method.GET, AppConstants.GET_SOURCES, null, { response ->
                try {
                    photosList.clear()
                    val photosArray: JSONArray = response.getJSONArray("sources")
                    var i = 0
                    while (i < photosArray.length()) {
                        val photosObject: JSONObject = photosArray.getJSONObject(i)
                        val picturesModel = PicturesModel(
                            photosObject.getInt("source_id"),
                            photosObject.getString("source_link"),
                            photosObject.getString("title"),
                            photosObject.getString("created_at"),
                            photosObject.optString("details")
                        )
                        photosList.add(picturesModel)
                        i++
                    }
                    Log.d("PhotosArrayList", photosList.toString())
                    picturesAdapter.notifyDataSetChanged()
                    if (photosProgressBar != null) {
                        photosProgressBar.visibility = View.GONE
                        if (photosList.size == 0) {
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
                    photosProgressBar.visibility = View.GONE
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
                val pref = getSharedPreferences(BaseApplication.PREFS, Context.MODE_PRIVATE)
                headers["type"] = "images"
                headers["languages-code"] = pref?.getString(BaseApplication.LOCALE, "ar") as String
                return headers
            }
        }
        photosRequest.retryPolicy =
            DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        requestQueue.add(photosRequest)
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