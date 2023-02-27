package com.example.kmitlcompanion.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserEditData(
    val username : String?,
    val faculty : String?,
    val department : String?,
    val year : String?,
): Parcelable
