package com.example.kmitlcompanion.domain.model

import android.os.Parcelable
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationDetail(
    val place: String?,
    val address: String?,
    val point: Point,
) : Parcelable