package com.example.kmitlcompanion.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotiLogDetails(
    val id : Long,
    val name : String,
    val startTime : String,
    val endTime : String,
): Parcelable
