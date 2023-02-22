package com.example.kmitlcompanion.data.model

import com.mapbox.android.gestures.MoveDistancesObject

data class SearchDataDetails(
    val id: Int,
    val name : String,
    val place : String,
    val address : String,
    val pic : Int,
    val code : Int,
    val distance : String,
) {


}