package com.example.kmitlcompanion.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserSettingsData(
    val username : String?,
    val faculty : String?,
    val email : String?,
    val department : String?,
    val year : String?,
): Parcelable
