package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.content.Context
import android.content.Intent
import com.example.kmitlcompanion.background.LocationService
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.google.gson.Gson
import javax.inject.Inject

class ViewService @Inject constructor(

){
    fun setup(context: Context , viewModel: MapboxViewModel) {
        val intent = Intent(context,LocationService::class.java)
        val eventListJson = Gson().toJson(viewModel.mapEventResponse.value!!.eventPoints)
        intent.putExtra("event",eventListJson)
        context.startService(intent)

    }
}