package com.example.kmitlcompanion.data.model

import com.mapbox.geojson.Point


data class EventAreaData (
    val name:String,
    val description : String,
    val id: Long,
    val startTime : String,
    val endTime : String,
    val area : List<Point>,
    val imageLink : List<String>,
)