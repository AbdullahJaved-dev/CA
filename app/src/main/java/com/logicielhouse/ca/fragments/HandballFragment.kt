package com.logicielhouse.ca.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.logicielhouse.ca.R
import com.logicielhouse.ca.adapter.TablePointsAdapter
import com.logicielhouse.ca.model.TablePointsModel
import kotlinx.android.synthetic.main.fragment_handball.*


class HandballFragment : Fragment(R.layout.fragment_handball) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvHandballPoints.apply {
            adapter = TablePointsAdapter(TablePointsModel.pointsList)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)

        }
    }
}