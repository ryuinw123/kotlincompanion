package com.example.kmitlcompanion.background

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.kmitlcompanion.domain.model.EventArea
import com.example.kmitlcompanion.ui.mapboxview.broadcast.GeofenceReceiver
import com.mapbox.geojson.MultiPolygon
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.turf.TurfJoins
import com.mapbox.turf.TurfMeasurement
import javax.inject.Inject

class SecretPolygon @Inject constructor(

) {

    private val cancelArea = mutableListOf<EventArea>()
    private lateinit var areas: List<EventArea>
    fun start(eventAreas : List<EventArea>) {
        //this.areas = areas
        //point = 37.3708, -122.0358
        //D/createLocation: position = EventDetail(point=[Point{type=Point, bbox=null, coordinates=[-122.09368411555363, 37.40996715600657]}, Point{type=Point, bbox=null, coordinates=[-122.09969165409512, 37.346488595544415]}, Point{type=Point, bbox=null, coordinates=[-122.01129031297978, 37.34491406444812]}, Point{type=Point, bbox=null, coordinates=[-122.00380146356501, 37.411567921087695]}])
        //point = 37.3115, -122.2649
        //position = EventDetail(point=[Point{type=Point, bbox=null, coordinates=[-122.30423322839806, 37.30426986660123]}, Point{type=Point, bbox=null, coordinates=[-122.3051290196735, 37.347568252831465]}, Point{type=Point, bbox=null, coordinates=[-122.2222716187394, 37.33655878832518]}, Point{type=Point, bbox=null, coordinates=[-122.22474129882005, 37.29296928749751]}])
        areas = eventAreas
    }

    fun filterArea(context: Context,point : Point) {

        //Log.d("Geofence" , "EnterGeofence")

        for (area in areas) {
            val polygon = Polygon.fromLngLats(listOf(area.area))
            if (TurfJoins.inside(point,polygon)) {
                if (area !in cancelArea) {
                    val intent = Intent(context, GeofenceReceiver::class.java)
                    intent.action = "com.example.geofence.TRANSITION"
                    intent.putExtra("id", area.id)
                    intent.putExtra("name", area.name)
                    intent.putExtra("startTime", area.startTime)
                    intent.putExtra("startTime", area.endTime)
                    context.sendBroadcast(intent)
                    cancelArea.add(area)
                    //Log.d("Geofence" , "Inside Area")
                }
            }
        }

    }

}