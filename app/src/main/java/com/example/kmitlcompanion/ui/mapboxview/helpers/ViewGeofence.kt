package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.kmitlcompanion.domain.model.MapInformation
import com.example.kmitlcompanion.domain.model.MapPoint
import com.example.kmitlcompanion.ui.mapboxview.broadcast.GeofenceReceiver
import com.example.kmitlcompanion.ui.mapboxview.broadcast.NotificationUtils
import com.example.kmitlcompanion.ui.mapboxview.utils.ToasterUtil
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.Serializable
import javax.inject.Inject

class ViewGeofence @Inject constructor(
    private val context: Context,
    private val toasterUtil: ToasterUtil,
    private val notificationUtils: NotificationUtils
) {
//    private lateinit var geofencingClient: GeofencingClient
//    private lateinit var geofencePendingIntent: PendingIntent

//    fun setup(mapInformation : MapInformation) {
//        geofencingClient = LocationServices.getGeofencingClient(context)
//        val intent = Intent(context, GeofenceReceiver::class.java)
//        intent.action = "com.example.geofence.TRANSITION"
//        val gson = Gson()
//        val locationListJson = gson.toJson(mapInformation.mapPoints)
//        intent.putExtra("location_list", locationListJson)
//        Log.d("Geofence" , mapInformation.mapPoints.toString())
//        geofencePendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        notificationUtils.createChannel(NotificationUtils.GEO_CHANNEL_ID)
//        run(mapInformation.mapPoints)
//    }
//
//    private fun run(mapPoint : List<MapPoint>) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//            generateGeofenceLocation(mapPoint)
//        }
//        else {
//            Dexter.withContext(context)
//                .withPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//                .withListener(object : PermissionListener {
//
//                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
//                        generateGeofenceLocation(mapPoint)
//                    }
//
//                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
//                        if (response!!.isPermanentlyDenied) {
//                            toasterUtil.showToast("Permission Permanently Denied")
//                        }
//                    }
//
//                    override fun onPermissionRationaleShouldBeShown(
//                        permission: PermissionRequest?,
//                        token: PermissionToken?
//                    ) {
//                        token?.continuePermissionRequest()
//                    }
//                }
//                ).check()
//        }
//    }
//
//    private fun generateGeofenceLocation(mapInformation : List<MapPoint>) {
//        val geofenceList =  mutableListOf<Geofence>()
//        mapInformation.forEach {
//            val locationRadius = 1020f
//            val location = Geofence.Builder()
//                .setRequestId(it.id.toString())
//                .setCircularRegion(it.latitude,it.longitude,locationRadius)
//                .setExpirationDuration(Geofence.NEVER_EXPIRE)
//                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
//                .build()
//            geofenceList.add(location)
//        }
//        createGeofenceLocation(geofenceList)
//    }
//
//    private fun createGeofenceLocation(geofenceList : MutableList<Geofence>) {
//        val geofenceRequest = GeofencingRequest.Builder()
//            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
//            .addGeofences(geofenceList)
//            .build()
//
//        removeGeofence().run {
//            addGeofence(geofenceRequest,geofencePendingIntent)
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun addGeofence(geofenceRequest: GeofencingRequest,geofencePendingIntent: PendingIntent) {
//        geofencingClient.addGeofences(geofenceRequest, geofencePendingIntent)
//            .addOnSuccessListener {
//                Log.d("Geofence" , "Geofence Added")
//            }
//            .addOnFailureListener {
//                Log.d("Geofence" , "Geofence Added Fail")
//                Log.d("Geofence Exception", it.toString())
//            }
//    }
//
//    private fun removeGeofence() {
//        geofencingClient.removeGeofences(geofencePendingIntent).run {
//            addOnSuccessListener { Log.d("Geofence" , "Geofence Removed") }
//            addOnFailureListener { Log.d("Geofence" , "Geofence Removed Fail") }
//        }
//    }
}