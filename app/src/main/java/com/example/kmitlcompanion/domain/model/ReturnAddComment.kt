package com.example.kmitlcompanion.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReturnAddComment(
    val commentId: Int,
    val author: String,
): Parcelable
