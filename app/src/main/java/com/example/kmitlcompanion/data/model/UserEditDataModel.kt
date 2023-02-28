package com.example.kmitlcompanion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserEditDataModel(
    val username : String?,
    val faculty : String?,
    val department : String?,
    val year : String?,
): Parcelable
