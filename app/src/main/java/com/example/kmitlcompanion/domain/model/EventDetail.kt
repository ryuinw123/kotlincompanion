package com.example.kmitlcompanion.domain.model

import android.os.Parcelable
import com.mapbox.geojson.Point
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventDetail (
    val point: List<Point>,
) : Parcelable
