package com.logicielhouse.ca.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.NetworkResponse
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.logicielhouse.ca.AppConstants
import com.logicielhouse.ca.BaseApplication
import com.logicielhouse.ca.R
import com.logicielhouse.ca.adapter.TablePointsAdapter
import com.logicielhouse.ca.displayMessage
import com.logicielhouse.ca.model.TablePointsModel
import kotlinx.android.synthetic.main.fragment_others.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class OthersFragment : Fragment(R.layout.fragment_others) {
    private var pointsList: ArrayList<TablePointsModel> = ArrayList()
    private lateinit var tablePointsAdapter: TablePointsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTablePoints()
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

                    rvOthersPoints.apply {
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
}