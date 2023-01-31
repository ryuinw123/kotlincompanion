package com.example.kmitlcompanion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReturnAddCommentData(
    val commentId: Int,
    val author: String,
): Parcelable
