package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.content.Context
import android.content.Intent
import com.example.kmitlcompanion.background.LocationService
import javax.inject.Inject

class ViewService @Inject constructor(

){
    fun setup(context: Context) {

        context.startService(Intent(context,LocationService::class.java))

    }
}