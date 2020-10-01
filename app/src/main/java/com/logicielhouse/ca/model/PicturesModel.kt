package com.logicielhouse.ca.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Abdullah on 9/27/2020.
 */

@Parcelize
data class PicturesModel(
    val id: Int = 0,
    val pictureURI: String = "",
    val pictureTitle: String = "",
    val pictureDate: String = "",
    val pictureDescription: String = ""
) : Parcelable {
    companion object {
        /*  val picturesList= mutableListOf(
              PicturesModel(1, R.drawable.image_dummy,"West wood Signs New Three-Year Deal","24th Sept",R.string.dummy_string),
              PicturesModel(2, R.drawable.image_dummy,"West wood Signs New Three-Year Deal","24th Sept",R.string.dummy_string),
              PicturesModel(3, R.drawable.image_dummy,"West wood Signs New Three-Year Deal","24th Sept",R.string.dummy_string),
              PicturesModel(4, R.drawable.image_dummy,"West wood Signs New Three-Year Deal","24th Sept",R.string.dummy_string)
          )*/
    }

}