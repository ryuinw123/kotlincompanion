package com.example.kmitlcompanion.ui.mapboxview.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GeofenceReceiver : BroadcastReceiver() {

    @Inject lateinit var notificationUtils: NotificationUtils
    @Inject lateinit var saveNotificationUtils: SaveNotificationUtils

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.geofence.TRANSITION") {
            val id = intent.getLongExtra("id" , 0)
            val name = intent.getStringExtra("name")

            Log.d("test_noti" , "ฉัน อารายา")
            notificationUtils.sendGeofenceEnteredNotification(id , name ?: "Error",NotificationUtils.GEO_CHANNEL_ID)
            saveNotificationUtils.saveNotificationToCache(intent)
            Log.d("Geofence" , "Transition In")
        }
    }
}