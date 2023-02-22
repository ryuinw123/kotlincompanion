package com.example.kmitlcompanion.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PinEventDetail(
    val eventLikeCounting : Int,
    val isEventLiked : Boolean,
    val isEventBookmarked : Boolean,
    val isMyPin : Boolean,
    val createdUserName : String,
) : Parcelable
