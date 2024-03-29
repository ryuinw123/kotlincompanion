package com.example.kmitlcompanion.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
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
