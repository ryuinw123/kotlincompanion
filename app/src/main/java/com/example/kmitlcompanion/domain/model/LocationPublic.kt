package com.example.kmitlcompanion.domain.model

import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import com.mapbox.geojson.Point
import kotlinx.parcelize.Parcelize
import java.io.File
import java.net.URI

@Parcelize
data class LocationPublic(
    val inputName:String?,
    val description:String?,
    val place: String?,
    val type: String?,
    val address: String?,
    val point: Point?,
    val file: List<File?>,
    val uri: List<Uri?>,
) : Parcelable