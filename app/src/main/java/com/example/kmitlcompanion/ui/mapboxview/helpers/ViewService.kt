package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.example.kmitlcompanion.domain.model.MapInformation
import com.example.kmitlcompanion.foreground.MapService
import javax.inject.Inject

class ViewService @Inject constructor(
    private val context: Context
){

    private lateinit var connection: ServiceConnection
    private lateinit var mapService : MapService
    private var isBound = false

    fun setup() {

        connection = object : ServiceConnection{
            override fun onServiceConnected(classname: ComponentName?, service: IBinder?) {
                val binder = service as MapService.MapBinder
                Log.d("Service" , "mapService Create")
                mapService = binder.getService()
                isBound = true

            }

            override fun onServiceDisconnected(classname: ComponentName?) {
                isBound = false
            }

        }

        if (!isBound) {
            Log.d("MapService" , "Service Inject")
            val intent = Intent(context,MapService::class.java)
            context?.bindService(intent , connection , Context.BIND_AUTO_CREATE)
        }

    }

}