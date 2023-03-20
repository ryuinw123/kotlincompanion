package com.example.kmitlcompanion.domain.model

import com.mapbox.geojson.Point


data class EventArea(
    val name:String,
    val description : String,
    val id: Long,
    val startTime : String,
    val endTime : String,
    val area : List<Point>,
    val imageLink : List<String>,
    val type : Int,
    val url : List<String>,
)