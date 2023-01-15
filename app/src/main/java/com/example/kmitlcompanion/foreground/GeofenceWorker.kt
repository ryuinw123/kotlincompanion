package com.example.kmitlcompanion.foreground

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.kmitlcompanion.domain.model.MapPoint
import com.example.kmitlcompanion.ui.mapboxview.broadcast.GeofenceReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Tasks
import com.google.common.util.concurrent.ListenableFuture
import org.json.JSONArray
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GeofenceWorker (context: Context,workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val geofencingClient = LocationServices.getGeofencingClient(applicationContext)
    private lateinit var pendingIntent: PendingIntent
    private lateinit var geofenceRequest: GeofencingRequest

    private val locationListJson = workerParams.inputData.getString("location_list")
    private val locationList = mutableListOf<MapPoint>()
    private val geofenceList =  mutableListOf<Geofence>()

    init {
        convertJsonArrayToList()
    }

    private fun convertJsonArrayToList() {
        val jsonArray = JSONArray(locationListJson)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val mapPoint = MapPoint(
                id = jsonObject.getLong("id"),
                latitude = jsonObject.getDouble("latitude"),
                longitude = jsonObject.getDouble("longitude"),
                description = "",
                address = "",
                imageLink = listOf(),
                type = "",
                place = ""
            )
            locationList.add(mapPoint)
        }
    }


    private fun generateGeofenceLocation() {

        locationList.forEach {
            val locationRadius = 5000f
            val location = Geofence.Builder()
                .setRequestId(it.id.toString())
                .setCircularRegion(it.latitude,it.longitude,locationRadius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build()

            geofenceList.add(location)

        }
    }

    private fun createGeofenceLocation() {
        geofenceRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofences(geofenceList)
            .build()
        val intent = Intent(applicationContext, GeofenceReceiver::class.java)
        intent.action = "com.example.geofence.TRANSITION"
        pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }


    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        Log.d("GeofenceWorker" , "Success Create List $locationList")

        generateGeofenceLocation()

        Log.d("GeofenceWorker" , "Success Generate Location $geofenceList")
        createGeofenceLocation()
        Log.d("GeofenceWorker" , "Success Create Location ${geofenceRequest.geofences}")
        geofencingClient.addGeofences(geofenceRequest, pendingIntent)
            .addOnSuccessListener {
                Log.d("GeofenceWorker", "Geofence added successfully")
            }
            .addOnFailureListener { exception ->
                Log.e("GeofenceWorker", "Error adding geofence: ${exception.message}")
            }
        return Result.success()
    }
}