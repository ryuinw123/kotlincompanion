package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.kmitlcompanion.domain.model.MapInformation
import com.example.kmitlcompanion.foreground.MainService
import com.example.kmitlcompanion.ui.mapboxview.utils.ToasterUtil
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.mapbox.android.core.permissions.PermissionsListener
import javax.inject.Inject

class ViewService @Inject constructor(
    private val context: Context,
    private val toasterUtil: ToasterUtil
){

    private lateinit var connection: ServiceConnection
    private lateinit var mainService: MainService
    private var isBound = false

    fun setup() {
        Dexter.withContext(context)
            .withPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            .withListener(object : PermissionListener {

                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    addConnection()
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

    fun addConnection() {
        connection = object : ServiceConnection{
            override fun onServiceConnected(classname: ComponentName?, service: IBinder?) {
                val binder = service as MainService.ServiceBinder
                Log.d("Service" , "mapService Create")
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
    }

}