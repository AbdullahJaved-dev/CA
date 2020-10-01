package com.logicielhouse.ca.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Abdullah on 9/27/2020.
 */
@Parcelize
data class VideosModel(
    val id: Int = 0,
    val videoThumbnail: String = "",
    val videoURL: String = "",
    val videoTitle: String = "",
    val videoDate: String = "",
    val videoDescription: String = ""
) : Parcelable {
    companion object {
        /* val videosList = mutableListOf(
             VideosModel(
                 1,
                 R.drawable.image_dummy,
                 "",
                 "West wood Signs New Three-Year Deal",
                 "24th Sept",
                 "video Club",
                 R.string.dummy_string
             ),
             VideosModel(
                 2,
                 R.drawable.image_dummy,
                 "",
                 "West wood Signs New Three-Year Deal",
                 "24th Sept",
                 "video Club",
                 R.string.dummy_string
             ),
             VideosModel(
                 3,
                 R.drawable.image_dummy,
                 "",
                 "West wood Signs New Three-Year Deal",
                 "24th Sept",
                 "video Club",
                 R.string.dummy_string
             ),
             VideosModel(
                 4,
                 R.drawable.image_dummy,
                 "",
                 "West wood Signs New Three-Year Deal",
                 "24th Sept",
                 "video Club",
                 R.string.dummy_string
             ),
             VideosModel(
                 5,
                 R.drawable.image_dummy,
                 "",
                 "West wood Signs New Three-Year Deal",
                 "24th Sept",
                 "video Club",
                 R.string.dummy_string
             ),
             VideosModel(
                 6,
                 R.drawable.image_dummy,
                 "",
                 "West wood Signs New Three-Year Deal",
                 "24th Sept",
                 "video Club",
                 R.string.dummy_string
             )
         )*/
    }

}