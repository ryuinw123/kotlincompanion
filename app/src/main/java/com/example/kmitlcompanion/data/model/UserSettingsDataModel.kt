package com.example.kmitlcompanion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserSettingsDataModel(
    val username : String?,
    val faculty : String?,
    val email : String?,
    val department : String?,
    val year : String?,
): Parcelable
