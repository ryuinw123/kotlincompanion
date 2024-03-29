package com.example.kmitlcompanion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PinData(
    val likeCounting : Int,
    val isLiked : Boolean,
    val comment : MutableList<CommentData>,
    val isBookmarked : Boolean,
    val isMyPin : Boolean,
    val createdUserName : String,
) : Parcelable
