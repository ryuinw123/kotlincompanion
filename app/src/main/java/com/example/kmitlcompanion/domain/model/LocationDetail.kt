package com.example.kmitlcompanion.domain.model

import com.mapbox.geojson.Point

data class LocationDetail(
    val place: String,
    val address: String,
    val point: Point
)