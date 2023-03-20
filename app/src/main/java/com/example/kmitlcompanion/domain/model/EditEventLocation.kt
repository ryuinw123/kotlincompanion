package com.example.kmitlcompanion.domain.model

import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import com.mapbox.geojson.Point
import kotlinx.parcelize.Parcelize
import java.io.File
import java.net.URI

@Parcelize
data class EditEventLocation(
    val eventId:String?,
    val name: String?,
    val description:String?,
    val image : MutableList<Pair<Int,Any>?>,
    val type : Int?,
    val url : String?,
) : Parcelable