package com.example.kmitlcompanion.ui.mapboxview.broadcast

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.kmitlcompanion.domain.model.MapPoint
import com.google.android.gms.location.GeofencingEvent
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import javax.inject.Inject

@AndroidEntryPoint
class GeofenceReceiver : BroadcastReceiver() {

    @Inject lateinit var notificationUtils: NotificationUtils

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.geofence.TRANSITION") {
            val locationId = GeofencingEvent.fromIntent(intent)!!.triggeringGeofences!![0].requestId.toLong()
            Log.d("Geofence","Request Id = $locationId")
            val locationListJson = intent.getStringExtra("location_list")
            val locationList = ArrayList<MapPoint>()
            val jsonArray = JSONArray(locationListJson)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val latitude = jsonObject.getDouble("latitude")
                val longitude = jsonObject.getDouble("longitude")
                val place = jsonObject.getString("place")
                val id = jsonObject.getLong("id")
                locationList.add(MapPoint(
                    name = "",
                    description = "",
                    address = "",
                    place = place,
                    imageLink = listOf(),
                    type = "",
                    id = id,
                    latitude = latitude,
                    longitude = longitude,
                ))
            }

            val location = locationList.single { item ->
                item.id == locationId
            }

            notificationUtils.sendGeofenceEnteredNotification(locationId , location.place)
            Log.d("Geofence" , "Transition In")
        }
    }
}