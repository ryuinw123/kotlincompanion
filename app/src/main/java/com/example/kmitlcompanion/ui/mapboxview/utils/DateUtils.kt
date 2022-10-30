package com.example.kmitlcompanion.ui.mapboxview.utils

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DateUtils @Inject constructor() {
    fun getTime() : String {
        val calendar = Calendar.getInstance(Locale.ROOT).time
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        return formatter.format(calendar)
    }
}