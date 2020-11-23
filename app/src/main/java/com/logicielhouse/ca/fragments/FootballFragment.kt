package com.logicielhouse.ca.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.logicielhouse.ca.BaseApplication
import com.logicielhouse.ca.R
import com.logicielhouse.ca.adapter.TablePointsAdapter
import com.logicielhouse.ca.model.TablePointsModel
import com.logicielhouse.ca.utils.AppConstants
import com.logicielhouse.ca.utils.displayMessage
import kotlinx.android.synthetic.main.fragment_football.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class FootballFragment : Fragment(R.layout.fragment_football) {
    private var pointsList: ArrayList<TablePointsModel> = ArrayList()
    private lateinit var tablePointsAdapter: TablePointsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        getTablePoints()
        swipeRefresh.setOnRefreshListener {
            footballProgressBar.visibility = View.VISIBLE
            pointsList.clear()
            getTablePoints()
            swipeRefresh.isRefreshing = false
        }
    }

    private fun setupUI() {
        tablePointsAdapter = TablePointsAdapter(pointsList)
        rvFootballPoints.apply {
            adapter = tablePointsAdapter
            layoutManager =
                LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
        }
    }

    private fun getTablePoints() {
        val pointsRequest = object :
            JsonObjectRequest(Method.GET, AppConstants.GET_POINTS, null, { response ->
                try {
                    pointsList.clear()
                    val pointsArray: JSONArray = response.getJSONArray("club_point")

                    for (i in 0 until pointsArray.length()) {
                        val pointsObject: JSONObject = pointsArray.getJSONObject(i)
                        val pointsModel = TablePointsModel(
                            pointsObject.optInt("club_id"),
                            pointsObject.optInt("position"),
                            pointsObject.getString("club_logo_uri"),
                            pointsObject.getString("club_name"),
                            pointsObject.optInt("gamePlayed"),
                            pointsObject.optInt("goalDifference"),
                            pointsObject.optInt("Points"),
                            pointsObject.optInt("rankStatus")
                        )
                        pointsList.add(pointsModel)
                    }

                    tablePointsAdapter.notifyDataSetChanged()
                    if (footballProgressBar != null) {
                        footballProgressBar.visibility = View.GONE
                        if (pointsList.size == 0) {
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
                    footballProgressBar?.visibility = View.GONE
                    val response: NetworkResponse = error.networkResponse
                    if (response.data != null) {
                        val errorObj = JSONObject(String(response.data))
                        displayMessage(requireActivity(), errorObj.optString("message"))
                    }
                } catch (e: Exception) {
                    displayMessage(requireActivity(), getString(R.string.unknown_error))
                }
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                val pref =
                    activity?.getSharedPreferences(BaseApplication.PREFS, Context.MODE_PRIVATE)
                headers["languages-code"] = pref?.getString(BaseApplication.LOCALE, "ar") as String
                headers["game_name"] = "football"
                return headers
            }
        }
        pointsRequest.retryPolicy =
            DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(pointsRequest)
    }
}