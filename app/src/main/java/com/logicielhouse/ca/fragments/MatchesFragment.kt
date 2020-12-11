package com.logicielhouse.ca.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.logicielhouse.ca.BaseApplication
import com.logicielhouse.ca.R
import com.logicielhouse.ca.adapter.MatchesAdapters
import com.logicielhouse.ca.model.MatchesModel
import com.logicielhouse.ca.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_matches.*
import org.json.JSONException
import org.json.JSONObject


class MatchesFragment : Fragment(R.layout.fragment_matches),
    MatchesAdapters.MatchTicketClickListener {
    private lateinit var rvMatches: RecyclerView

    companion object {
        fun newInstance() = MatchesFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).bottomNavigation.menu.getItem(4).isChecked = true
        (activity as MainActivity).setToolbarTitle(getString(R.string.matches))

        rvMatches = view.findViewById(R.id.rvMatches)

        matchesRequest()
    }

    private fun matchesRequest() {
        val url = "http://burnlyfc.haseebihsan.com/api/getMatchFixture"
        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                if (isAdded) {
                    try {
                        println("API Response is: $response")
                        extractData(JSONObject(response))

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            },
            Response.ErrorListener { error ->
                if (isAdded) {
                    try {
                        matchesProgressBar.visibility = View.GONE
                        Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT)
                            .show()
                        println("API Error is:$error")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                val pref =
                    requireActivity().getSharedPreferences(
                        BaseApplication.PREFS,
                        Context.MODE_PRIVATE
                    )
                headers["languages-code"] = pref.getString(BaseApplication.LOCALE, "ar") as String
                return headers
            }
        }
        stringRequest.retryPolicy =
            DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

    fun extractData(jsonObject: JSONObject) {
        val matchesList: MutableList<MatchesModel> = ArrayList()

        if (jsonObject.has("success")) {
            if (jsonObject.getString("success") == "1") {
                val matchFixturesArr = jsonObject.getJSONArray("match_fixtures")
                for (index in 0 until matchFixturesArr.length()) {
                    val matchObj = matchFixturesArr.getJSONArray(index).getJSONObject(0)
                    val team1_name = matchObj.getString("team1_name")
                    val team1_logo = matchObj.getString("team1_logo")
                    val team2_name = matchObj.getString("team2_name")
                    val team2_logo = matchObj.getString("team2_logo")
                    val time_schedule = matchObj.getString("time_sechudel")
                    val match_description = matchObj.getString("match_description")
                    val matchResult = matchObj.getString("result")
                    matchesList.add(
                        MatchesModel(
                            team1_name,
                            team1_logo,
                            team2_name,
                            team2_logo,
                            time_schedule,
                            match_description,
                            matchResult
                        )
                    )
                }
            } else {
                Toast.makeText(requireActivity(), "Oops! Something went Wrong!", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(requireActivity(), "Oops! Nothing found!", Toast.LENGTH_SHORT).show()
        }
        val matchesAdapters = MatchesAdapters(matchesList, this)

        if (isAdded) {
            matchesProgressBar.visibility = View.GONE
            rvMatches.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            rvMatches.adapter = matchesAdapters
        }
    }

    override fun onMatchTicketClickListener() {
    }
}