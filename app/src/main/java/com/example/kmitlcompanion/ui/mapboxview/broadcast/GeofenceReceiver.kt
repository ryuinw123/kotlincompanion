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
            val id = intent.getLongExtra("id" , 0)
            val name = intent.getStringExtra("name")

            notificationUtils.sendGeofenceEnteredNotification(id , name ?: "Error",NotificationUtils.GEO_CHANNEL_ID)
            Log.d("Geofence" , "Transition In")
        }
    }
}