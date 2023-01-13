package com.example.kmitlcompanion.foreground

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.GeofencingEvent

class GeofenceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.geofence.TRANSITION") {
            val locationId = GeofencingEvent.fromIntent(intent)!!.triggeringGeofences!![0].requestId
            val notification = GeofenceNotification().buildNotification(context, locationId)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, notification)
            Log.d("Geofence" , "Transition In")
        }
    }
}