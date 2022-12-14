package com.example.kmitlcompanion.data.util

import java.util.*
import javax.inject.Inject

class TimeUtils @Inject constructor() {
    val currentTime: Long
        get() = Calendar.getInstance().timeInMillis
}