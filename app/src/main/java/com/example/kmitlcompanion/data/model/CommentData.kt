package com.example.kmitlcompanion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommentData(
    val id: Int,
    val date: String,
    val author: String,
    val message: String,
    val like: Int,
    val dislike : Int,
    val isLikedComment: Boolean,
    val isDisLikedComment: Boolean,
    val myComment:Boolean
): Parcelable
