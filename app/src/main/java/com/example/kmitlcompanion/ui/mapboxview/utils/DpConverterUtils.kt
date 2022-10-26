package com.example.kmitlcompanion.ui.mapboxview.utils

import android.app.Application
import android.content.Context
import android.util.DisplayMetrics
import javax.inject.Inject


class DpConverterUtils @Inject constructor(private val context: Context){
    fun dpToPixel(dp: Float): Float {
        val metrics: DisplayMetrics = context.resources.displayMetrics
        return dp * (metrics.densityDpi / 160f)
    }

}