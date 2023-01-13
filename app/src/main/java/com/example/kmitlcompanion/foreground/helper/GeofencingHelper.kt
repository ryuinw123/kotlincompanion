package com.example.kmitlcompanion.foreground.helper

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.kmitlcompanion.domain.model.MapPoint
import com.example.kmitlcompanion.foreground.GeofenceReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import javax.inject.Inject

class GeofencingHelper @Inject constructor(
) {
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var geofencePendingIntent: PendingIntent

    fun setup(context: Context) {
        geofencingClient = LocationServices.getGeofencingClient(context)
    }

    fun generateGeofenceLocation(context: Context,mapInformation : List<MapPoint>) {

        val geofenceList =  mutableListOf<Geofence>()
        mapInformation.forEach {
            val locationRadius = 5000f
            val location = Geofence.Builder()
                .setRequestId(it.id.toString())
                .setCircularRegion(it.latitude,it.longitude,locationRadius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build()

            geofenceList.add(location)

        }

        createGeofenceLocation(context,geofenceList)
    }

    private fun createGeofenceLocation(context: Context, geofenceList : MutableList<Geofence>) {
        val geofenceRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofences(geofenceList)
            .build()
        val intent = Intent(context, GeofenceReceiver::class.java)
        intent.action = "com.example.geofence.TRANSITION"
        geofencePendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        addGeofence(geofenceRequest,geofencePendingIntent)
    }

    @SuppressLint("MissingPermission")
    private fun addGeofence(geofenceRequest: GeofencingRequest,geofencePendingIntent: PendingIntent) {
        geofencingClient.addGeofences(geofenceRequest, geofencePendingIntent)
            .addOnSuccessListener {
                Log.d("Geofence" , "Geofence Added")
            }
            .addOnFailureListener {
                Log.d("Geofence" , "Geofence Added Fail")
                Log.d("Geofence Exception", it.toString())
            }
    }

    private fun removeGeofence() {
        geofencingClient.removeGeofences(geofencePendingIntent)
            .addOnSuccessListener {
                Log.d("Geofence" , "Geofence Removed")
            }
            .addOnFailureListener {
                Log.d("Geofence" , "Geofence Removed Fail")
            }
    }


}