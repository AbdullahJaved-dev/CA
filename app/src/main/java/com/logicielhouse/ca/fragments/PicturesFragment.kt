package com.logicielhouse.ca.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.logicielhouse.ca.R
import com.logicielhouse.ca.adapter.PicturesAdapter
import com.logicielhouse.ca.model.PicturesModel
import kotlinx.android.synthetic.main.fragment_pivtures.*

class PicturesFragment : Fragment(R.layout.fragment_pivtures) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvPictures.apply {
            adapter = PicturesAdapter(
                listener = object : PicturesAdapter.PicturesAdapterClickListeners {
                    override fun viewPicture(pictureItem: PicturesModel) {

                    }
                }, PicturesModel.picturesList
            )
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }


}