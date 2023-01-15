package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.example.kmitlcompanion.domain.model.MapInformation
import com.example.kmitlcompanion.foreground.GeofenceWorkManager
import com.example.kmitlcompanion.ui.mapboxview.utils.ToasterUtil
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import javax.inject.Inject

class ViewService @Inject constructor(
    private val context: Context,
    private val toasterUtil: ToasterUtil
){

    private lateinit var connection: ServiceConnection
    //private lateinit var mainService: MainService
    private var isBound = false

    fun setup(mapInformation: MapInformation) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            //addConnection()
            GeofenceWorkManager.scheduleGeofenceWorker(mapInformation.mapPoints)
        }
        else {
            Dexter.withContext(context)
                .withPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                .withListener(object : PermissionListener {

                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        GeofenceWorkManager.scheduleGeofenceWorker(mapInformation.mapPoints)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        if (response!!.isPermanentlyDenied) {
                            toasterUtil.showToast("Permission Permanently Denied")
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                }
                ).check()
        }
    }

    /*fun addConnection() {
        connection = object : ServiceConnection{
            override fun onServiceConnected(classname: ComponentName?, service: IBinder?) {
                val binder = service as MainService.ServiceBinder
                Log.d("BackgroundService" , "mapService Create")
                mainService = binder.getService()
                isBound = true

            }

            override fun onServiceDisconnected(classname: ComponentName?) {
                isBound = false
            }

        }

        if (!isBound) {
            Log.d("MapService" , "Service Inject")
            val intent = Intent(context, MainService::class.java)
            context?.bindService(intent , connection , Context.BIND_AUTO_CREATE)
        }
    }*/

}