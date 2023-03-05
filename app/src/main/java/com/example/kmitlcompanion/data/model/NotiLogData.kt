package com.example.kmitlcompanion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotiLogData(
    val id : Long,
    val name : String,
    val startTime : String,
    val endTime : String,
): Parcelable
