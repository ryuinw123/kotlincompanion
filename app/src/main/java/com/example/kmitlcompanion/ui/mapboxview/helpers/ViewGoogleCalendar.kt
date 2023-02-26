package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.util.Log
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ViewGoogleCalendar @Inject constructor() {

    private lateinit var viewModel: MapboxViewModel
    private lateinit var context: Context

    fun setup(viewModel: MapboxViewModel,context: Context){
        this.viewModel = viewModel
        this.context = context
    }

    fun startGoogleCalendar(){

        val eventTitle = viewModel.nameLocationLabel.value
        val eventDescription = viewModel.descriptionLocationLabel.value
        val dateStringStart = viewModel.eventBindStart.value
        val dateStringEnd = viewModel.eventBindEnd.value

        val dateFormat1 = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val startTimeIn = dateFormat1.parse(dateStringStart)
        val startTime = Calendar.getInstance()
        startTime.time = startTimeIn
        val startTimeInMillis = startTime.timeInMillis


        val dateFormat2 = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val endTimeIn = dateFormat2.parse(dateStringEnd)
        val endTime = Calendar.getInstance()
        endTime.time = endTimeIn
        val endTimeInMillis = endTime.timeInMillis

        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, eventTitle)
            //putExtra(CalendarContract.Events.EVENT_LOCATION, "ที่บ้านของพวกเรา")
            putExtra(CalendarContract.Events.DESCRIPTION, eventDescription)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTimeInMillis)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTimeInMillis)
        }
        //Log.d("test_google",intent.toString())
        //Log.d("test_google","$eventTitle $eventDescription $startTimeInMillis $endTimeInMillis")
        context.startActivity(intent)
    }

}