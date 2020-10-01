package com.logicielhouse.ca.model

/**
 * Created by Abdullah on 9/27/2020.
 */
data class NewsModel(
    val id: Int = 0,
    val pictureURI: String = "",
    val title: String = "",
    val date: String = "",
    val description: String = "",
    val videoSource: String = "",
    val videoThumbnail: String = "",
    val videoURI: String = ""
) {
    companion object {
        /*val newsList = mutableListOf(
            NewsModel(
                1,
                R.drawable.image_dummy,
                "West wood Signs New Three-Year Deal",
                "24th August",
                R.string.dummy_string,
                "Club News",
                0,
                ""
            ),
            NewsModel(
                2,
                R.drawable.image_dummy,
                "West wood Signs New Three-Year Deal",
                "24th August",
                R.string.dummy_string,
                "Club News",
                0,
                ""
            ),
            NewsModel(
                3,
                R.drawable.image_dummy,
                "West wood Signs New Three-Year Deal",
                "24th August",
                R.string.dummy_string,
                "Club News",
                0,
                ""
            ),
            NewsModel(
                4,
                R.drawable.image_dummy,
                "West wood Signs New Three-Year Deal",
                "24th August",
                R.string.dummy_string,
                "Club News",
                0,
                ""
            ),
        )*/
    }

}