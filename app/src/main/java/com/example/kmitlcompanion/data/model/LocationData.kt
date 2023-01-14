package com.example.kmitlcompanion.data.model

import android.net.Uri
import com.mapbox.geojson.Point
import java.io.File
import java.net.URI

data class LocationData(
    val inputName:String,
    val description:String,
    val place: String,
    val type: String,
    val address: String,
    val latitude: Double,
    val longitude : Double,
    val file: File,
    val uri: Uri,
)