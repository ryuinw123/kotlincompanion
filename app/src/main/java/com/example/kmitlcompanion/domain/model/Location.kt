package com.example.kmitlcompanion.domain.model

import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import com.mapbox.geojson.Point
import kotlinx.parcelize.Parcelize
import java.io.File
import java.net.URI

@Parcelize
data class Location(
    val place: String?,
    val type: String?,
    val address: String?,
    val point: Point?,
    val file: File?,
    val uri: Uri?,
) : Parcelable