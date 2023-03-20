package com.example.kmitlcompanion.domain.model

import android.net.Uri
import com.mapbox.geojson.Point
import java.io.File

data class Event(
    val name:String?,
    val description:String?,
    val startTime : String?,
    val endTime : String?,
    val point: List<Point>,
    val file: List<File?>,
    val uri: List<Uri?>,
    val type : Int?,
    val url : String?
)
