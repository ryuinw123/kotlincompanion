package com.example.kmitlcompanion.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PinDetail(
    val likeCounting : Int,
    val isLiked : Boolean,
    val comment : MutableList<Comment>
) : Parcelable
