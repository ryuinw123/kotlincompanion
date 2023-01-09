package com.example.kmitlcompanion.foreground

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.location.*
import com.mapbox.geojson.Point

class SecretLocation(
    mapService: MapService
) : DefaultLifecycleObserver {
    companion object {
        const val LOCATION_UPDATE_INTERVAL: Long = 1000 // 5 seconds
        const val LOCATION_UPDATE_FASTEST_INTERVAL: Long = 1000 // 1 second
    }

    private val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mapService)
    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                val point = Point.fromLngLat(location.longitude,location.latitude)
                mapService.location = point
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun run() {
        val locationRequest = LocationRequest.create().apply {
            interval = LOCATION_UPDATE_INTERVAL
            fastestInterval = LOCATION_UPDATE_FASTEST_INTERVAL
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }



    @SuppressLint("MissingPermission")
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

        val locationRequest = LocationRequest.create().apply {
            interval = LOCATION_UPDATE_INTERVAL
            fastestInterval = LOCATION_UPDATE_FASTEST_INTERVAL
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}