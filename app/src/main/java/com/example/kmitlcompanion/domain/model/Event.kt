package com.example.kmitlcompanion.domain.model

import android.net.Uri
import com.mapbox.geojson.Point
import java.io.File

data class Event(
    val inputName:String?,
    val description:String?,
    val status : String?,
    val point: List<Point>,
    val file: List<File?>,
    val uri: List<Uri?>,
)
