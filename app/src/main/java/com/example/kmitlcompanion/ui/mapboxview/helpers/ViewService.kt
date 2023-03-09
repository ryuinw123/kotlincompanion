package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.kmitlcompanion.background.LocationService
import com.example.kmitlcompanion.domain.model.EventArea
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.example.kmitlcompanion.ui.mapboxview.utils.DateUtils
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class ViewService @Inject constructor(
    private val dateUtils: DateUtils,
) {
    fun setup(context: Context , viewModel: MapboxViewModel) {
        val intent = Intent(context,LocationService::class.java)

        val eventPoints = viewModel.mapEventResponse.value?.eventPoints
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val nowTime = format.parse(dateUtils.shinGetTime())
        val filteredEventPoints = eventPoints?.filter { format.parse(it.startTime) < nowTime }?.toMutableList() ?: mutableListOf()
//        Log.d("test_noti",nowTime.toString())
//        Log.d("test_noti",format.parse(eventPoints?.get(0)?.startTime).toString())
//        Log.d("test_noti",(format.parse(eventPoints?.get(0)?.startTime) < nowTime).toString())
//        eventPoints?.forEach {
//            val startEventTime = format.parse(it.startTime)
//            val nowTime = format.parse(dateUtils.shinGetTime())
//
//            if (startEventTime < nowTime){
//                filteredEventPoints.add(it)
//            }
//
//        }

        val eventListJson = Gson().toJson(filteredEventPoints.toList())
        intent.putExtra("event",eventListJson)
        context.startService(intent)
    }

    fun destroy(context : Context){
        val intent = Intent(context,LocationService::class.java)
        context.stopService(intent)
    }
}