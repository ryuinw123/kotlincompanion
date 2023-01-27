package com.example.kmitlcompanion.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LikeDetail(
    val likeCounting : Int,
    val isLiked : Boolean
) : Parcelable
