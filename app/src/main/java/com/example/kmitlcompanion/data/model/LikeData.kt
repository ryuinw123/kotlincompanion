package com.example.kmitlcompanion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LikeData(
    val likeCounting : Int,
    val isLiked : Boolean
) : Parcelable
