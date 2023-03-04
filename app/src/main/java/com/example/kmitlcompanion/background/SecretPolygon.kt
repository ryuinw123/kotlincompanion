package com.example.kmitlcompanion.background

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.kmitlcompanion.domain.model.EventArea
import com.example.kmitlcompanion.domain.model.NotiCollection
import com.example.kmitlcompanion.ui.mapboxview.broadcast.GeofenceReceiver
import com.example.kmitlcompanion.ui.mapboxview.utils.DateUtils
import com.mapbox.geojson.MultiPolygon
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.turf.TurfJoins
import com.mapbox.turf.TurfMeasurement
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SecretPolygon @Inject constructor(
    private val dateUtils: DateUtils,
) {


    private val cancelArea = mutableListOf<EventArea>()
    private lateinit var areas: List<EventArea>

    private val cancelTimeArea = mutableListOf<NotiCollection>()
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())


    fun start(eventAreas : List<EventArea>) {
        //this.areas = areas
        //point = 37.3708, -122.0358
        //D/createLocation: position = EventDetail(point=[Point{type=Point, bbox=null, coordinates=[-122.09368411555363, 37.40996715600657]}, Point{type=Point, bbox=null, coordinates=[-122.09969165409512, 37.346488595544415]}, Point{type=Point, bbox=null, coordinates=[-122.01129031297978, 37.34491406444812]}, Point{type=Point, bbox=null, coordinates=[-122.00380146356501, 37.411567921087695]}])
        //point = 37.3115, -122.2649
        //position = EventDetail(point=[Point{type=Point, bbox=null, coordinates=[-122.30423322839806, 37.30426986660123]}, Point{type=Point, bbox=null, coordinates=[-122.3051290196735, 37.347568252831465]}, Point{type=Point, bbox=null, coordinates=[-122.2222716187394, 37.33655878832518]}, Point{type=Point, bbox=null, coordinates=[-122.22474129882005, 37.29296928749751]}])
        areas = eventAreas
    }

    fun filterArea(context: Context,point : Point) {

//        Log.d("test_noti" , "EnterGeofence " + areas.size)

        for (area in areas) {
            val polygon = Polygon.fromLngLats(listOf(area.area))
            if (TurfJoins.inside(point,polygon)) {
                //val nowTime = format.parse(dateUtils.shinGetTime())
                val nowTime = Calendar.getInstance().time
                val eventIdLastTimeNoti = cancelTimeArea.firstOrNull{ it.id == area.id }

                if (eventIdLastTimeNoti != null && nowTime.time - eventIdLastTimeNoti.lastTime.time <= 180 * 60 * 1000) {
                    // Do nothing, notification already sent within last 10 minutes
                } else {
                    val intent = Intent(context, GeofenceReceiver::class.java).apply {
                        action = "com.example.geofence.EVENTAREA"
                        putExtra("id", area.id)
                        putExtra("name", area.name)
                        putExtra("startTime", area.startTime)
                        putExtra("startTime", area.endTime)
                    }
                    context.sendBroadcast(intent)
                    cancelTimeArea.removeAll { it.id == area.id }
                    cancelTimeArea.add(NotiCollection(area.id, Calendar.getInstance().time))
                    Log.d("test_noti" , "Inside Area")
                }

//                //if (area !in cancelArea) {
//                val intent = Intent(context, GeofenceReceiver::class.java)
//                intent.action = "com.example.geofence.TRANSITION"
//                intent.putExtra("id", area.id)
//                intent.putExtra("name", area.name)
//                intent.putExtra("startTime", area.startTime)
//                intent.putExtra("startTime", area.endTime)
//                context.sendBroadcast(intent)
//                //cancelArea.add(area)
//                Log.d("test_noti" , "Inside Area")
//                //}

            }
        }

    }

}