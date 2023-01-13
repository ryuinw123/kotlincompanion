package com.example.kmitlcompanion.foreground

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.example.kmitlcompanion.domain.model.MapInformation
import com.example.kmitlcompanion.foreground.helper.GeofencingHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainService : Service(){

    @Inject lateinit var geofencingHelper: GeofencingHelper
    @Inject lateinit var serviceUseCase: ServiceUseCase


    private val binder = ServiceBinder()

    var mapInformation : MapInformation? = null
        set(value) {
            field = value
            geofencingHelper.generateGeofenceLocation(this@MainService,value!!.mapPoints)
            Log.d("Geofence" , "MapInformation Downloaded")
        }


    override fun onCreate() {
        super.onCreate()
        serviceUseCase.setup(this)
        geofencingHelper.setup(this@MainService)

        Log.d("Service" , "Service Create")

        /*val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                // Log message
                Log.d("MyForegroundService", "Still Run")

                // Repeat every 5 seconds
                handler.postDelayed(this, 500)
            }
        }
        handler.post(runnable)*/
    }







    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }





    inner class ServiceBinder : Binder() {
        fun getService() : MainService = this@MainService
    }



    override fun onBind(intent: Intent?): IBinder? {

        serviceUseCase.downloadMap()

        return binder
    }
}