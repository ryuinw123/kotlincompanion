package com.example.kmitlcompanion.background

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    @Inject lateinit var secretLocation: SecretLocation
    override fun onCreate() {
        super.onCreate()
        Log.d("Geofence" , "Service Create")

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        secretLocation.run(this)
        return START_STICKY
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}