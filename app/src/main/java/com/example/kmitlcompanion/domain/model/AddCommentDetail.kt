package com.example.kmitlcompanion.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddCommentDetail(
    val markerId : String?,
    val message : String?
): Parcelable
