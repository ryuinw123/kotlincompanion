package com.example.kmitlcompanion.background

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.*


class LocationWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    private val TAG = "MyWorker"

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private val UPDATE_INTERVAL_IN_MILLISECONDS = 10000L

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
        UPDATE_INTERVAL_IN_MILLISECONDS / 2

    /**
     * The current location.
     */
    private var mLocation: Location? = null

    /**
     * Provides access to the Fused Location Provider API.
     */
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private lateinit var mContext: Context
    /**
        * Callback for changes in location.
        */
        private lateinit var mLocationCallback: LocationCallback

        /*override fun doWork(): Result {
            Log.d("Geofence", "doWork: Done")

            mContext = applicationContext
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
            mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                Log.d("Geofence" , "From callback Location Result = $locationResult")
            }

        }

        val mLocationRequest = LocationRequest.create().apply {
            interval = 5000 // 5 seconds
            fastestInterval = 3000 // 3 seconds
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        try {
            mFusedLocationClient.lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        mLocation = task.result
                        Log.d("Geofence", "Location : $mLocation")
                        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                    } else {
                        Log.w("Geofence", "Failed to get location.")
                    }
                }
        } catch (unlikely: SecurityException) {
            Log.e("Geofence", "Lost location permission." + unlikely)
        }

        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
        } catch (unlikely: SecurityException) {
            Log.e("Geofence", "Lost location permission. Could not request updates. " + unlikely)
        }

        return Result.success()
    }*/


    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        Log.d("Geofence" , "doWork")
        val locationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    Log.d("Geofence","Location Result $location")
                }
            }
        }
        val locationRequest = LocationRequest.create().apply {
            interval = 600000 // 5 seconds
            fastestInterval = 360000 // 3 seconds
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }
        locationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        return Result.success()
    }
}