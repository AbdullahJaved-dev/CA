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
import kotlinx.android.synthetic.main.fragment_basketball.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class BasketballFragment : Fragment(R.layout.fragment_basketball) {
    private var pointsList: ArrayList<TablePointsModel> = ArrayList()
    private var pointsList2: ArrayList<TablePointsModel> = ArrayList()
    private lateinit var tablePointsAdapter: TablePointsAdapter
    private lateinit var tablePointsAdapter2: TablePointsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        getTablePoints()
    }

    private fun setupUI() {
        tablePointsAdapter = TablePointsAdapter(pointsList)
        rvBasketballAPoints.apply {
            adapter = tablePointsAdapter
            layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
        }
        tablePointsAdapter2 = TablePointsAdapter(pointsList2)
        rvBasketballBPoints.apply {
            adapter = tablePointsAdapter2
            layoutManager =
                LinearLayoutManager(
                    requireContext(),
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
                    pointsList2.clear()
                    val pointsArray: JSONArray = response.getJSONArray("club_point")
                    val pointsTableObject: JSONObject = pointsArray.getJSONObject(0)
                    val pointsATableArray: JSONArray = pointsTableObject.getJSONArray("A")
                    val pointsBTableArray: JSONArray = pointsTableObject.getJSONArray("B")

                    for (i in 0 until pointsATableArray.length()) {
                        val pointsObject: JSONObject = pointsATableArray.getJSONObject(i)
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

                    for (j: Int in 0 until pointsBTableArray.length()) {
                        val pointsObject: JSONObject = pointsBTableArray.getJSONObject(j)
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
                        pointsList2.add(pointsModel)
                    }
                    tablePointsAdapter.notifyDataSetChanged()
                    tablePointsAdapter2.notifyDataSetChanged()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, { error ->
                try {
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
                headers["languages-code"] = pref?.getString(BaseApplication.LOCALE, "en") as String
                headers["game_name"] = "basketball"
                return headers
            }
        }
        pointsRequest.retryPolicy =
            DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(pointsRequest)
    }
}