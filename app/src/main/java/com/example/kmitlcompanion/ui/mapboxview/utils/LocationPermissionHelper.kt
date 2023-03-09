package com.example.kmitlcompanion.ui.mapboxview.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class LocationPermissionHelper @Inject constructor(
    @ActivityContext private val context: Context,
    private val toasterUtil: ToasterUtil,
    ) {
    private lateinit var permissionsManager: PermissionsManager

    fun checkPermissions(onMapReady: () -> Unit) {
        if (PermissionsManager.areLocationPermissionsGranted(context)) {
//            Log.d("test_permsiss","เข้า  checkPermissions if แรก")
            onMapReady()
        } else {
//            Log.d("test_permsiss","เข้า  checkPermissions if สอง")
            permissionsManager = PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: List<String>) {
//                    Log.d("test_permsiss","เอา permission มาไอสัส")
                    toasterUtil.showToast("ไม่สามารถขออนุญาติการเข้าถึงตำแหน่ง.")
//                    Toast.makeText(
//                        context, "",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
                override fun onPermissionResult(granted: Boolean) {
//                    Log.d("test_permsiss","ได้รับ permission แล้ว $granted")
                    if (granted) {
//                        Log.d("test_permsiss","ganted $granted")
                        onMapReady()
                    } else {
                        (context as Activity).finish()
                    }
                }
            })
            permissionsManager.requestLocationPermissions(context as Activity)
        }
    }


    fun checkPermissions(context: Context, activity: Activity, callBack: () -> Unit) {
        if (PermissionsManager.areLocationPermissionsGranted(context)) {
            callBack()
        } else {
            val permissionsManager = PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: List<String>) {
                    toasterUtil.showToast("ไม่สามารถขออนุญาติการเข้าถึงตำแหน่ง")
                    //open settings
                }
                override fun onPermissionResult(granted: Boolean) {
                    if (granted) {
                        callBack()
                    } else {
                        activity.finish()
                    }
                }
            })

            if ((!shouldShowRequestPermissionRationale(activity,Manifest.permission.ACCESS_FINE_LOCATION) ||
                 !shouldShowRequestPermissionRationale(activity,Manifest.permission.ACCESS_COARSE_LOCATION) ||
                 !shouldShowRequestPermissionRationale(activity,Manifest.permission.ACCESS_BACKGROUND_LOCATION))){
                permissionsManager.requestLocationPermissions(activity)
            }else{
                val builder = AlertDialog.Builder(activity)
                builder.setMessage("คุณต้องอนุญาติการเข้าถึงตำแหน่ง โดยการไปที่การตั้งค่าแอพพลิเคชัน")
                    .setPositiveButton("ไปที่ตั้งค่า") { _, _ ->
                        // Request permission again
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", activity.packageName, null)
                        intent.data = uri
                        activity.startActivity(intent)
                    }
                    .setCancelable(false)
                    .create()
                    .show()
            }
        }
    }

    fun checkPermissionsByGPT(activity: FragmentActivity,onMapReady: () -> Unit) {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted, do something
            onMapReady()
            Log.d("test_permsiss","Permission granted, do something")
        } else {
            val activity = activity as? FragmentActivity
            if (activity != null) {
                val permissionLauncher = activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    if (isGranted) {
                        // Permission granted, do something
                        onMapReady()
                        Log.d("test_permsiss","Permission granted, do something banana")
                    } else {
                        // Permission denied, show a message or take other action
                        Log.d("test_permsiss","// Permission denied, show a message or take other action")
                    }
                }
                // Permission not granted, request it
                Log.d("test_permsiss","// Permission not granted, request it")
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

}