package com.example.kmitlcompanion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PinEventData(
    val eventLikeCounting : Int,
    val isEventLiked : Boolean,
    val isEventBookmarked : Boolean,
    val isMyPin : Boolean,
    val createdUserName : String,
) : Parcelable
