package com.logicielhouse.ca.model

import com.logicielhouse.ca.R

/**
 * Created by Abdullah on 9/27/2020.
 */
data class TablePointsModel(
    val id: Int = 0,
    val position: Int = 0,
    val clubImage: Int = 0,
    val clubName: String = "",
    val gamePlayed: Int = 0,
    val goalDifference: Int = 0,
    val points: Int = 0,
    val rankStatus: String = ""
) {
    companion object {
        val pointsList = mutableListOf(
            TablePointsModel(1, 1, R.drawable.everton, "Everton", 12, 16, 30),
            TablePointsModel(2, 2, R.drawable.arsenal, "Arsenal", 12, 14, 28),
            TablePointsModel(3, 3, R.drawable.liverpool, "Leicester", 12, 12, 24),
            TablePointsModel(4, 4, R.drawable.everton, "Everton", 12, 7, 20),
            TablePointsModel(5, 5, R.drawable.arsenal, "Arsenal", 12, 5, 20),
            TablePointsModel(6, 6, R.drawable.liverpool, "Leicester", 12, 3, 18),
        )
    }
}