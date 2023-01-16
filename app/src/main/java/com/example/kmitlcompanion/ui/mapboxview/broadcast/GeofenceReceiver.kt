package com.example.kmitlcompanion.ui.mapboxview.broadcast

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.GeofencingEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GeofenceReceiver : BroadcastReceiver() {
    @Inject
    lateinit var geofenceNotification: GeofenceNotification

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.geofence.TRANSITION") {
            val locationId = GeofencingEvent.fromIntent(intent)!!.triggeringGeofences!![0].requestId
            val notification = geofenceNotification.buildNotification("1")
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, notification)
            Log.d("GeofenceWorker" , "Transition In")
        }
    }
}