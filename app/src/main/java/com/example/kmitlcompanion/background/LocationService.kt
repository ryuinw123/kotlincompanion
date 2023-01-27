package com.example.kmitlcompanion.background

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.example.kmitlcompanion.ui.mapboxview.broadcast.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    @Inject lateinit var secretLocation: SecretLocation
    @Inject lateinit var notificationUtils: NotificationUtils
    override fun onCreate() {
        super.onCreate()
        Log.d("Geofence" , "Service Create")

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notificationUtils.createChannel(NotificationUtils.LOCATION_CHANNEL_ID)
            secretLocation.start(this)
            val notification = notificationUtils.createNotification(NotificationUtils.LOCATION_CHANNEL_ID)
            startForeground(123, notification)

        }
        else
            secretLocation.start(this)
        return START_STICKY
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}