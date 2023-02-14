package com.example.kmitlcompanion.background

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.location.*
import com.mapbox.geojson.Point
import javax.inject.Inject

class SecretLocation @Inject constructor(
) {

    companion object {
        const val LOCATION_UPDATE_INTERVAL : Long = 5000
        const val LOCATION_UPDATE_FASTEST_INTERVAL : Long = 3000
    }
    lateinit var locationService : LocationService
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationCallback : LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            for (location in locationResult.locations) {
                //Log.d("Geofence","location from gps = $location")
                val point = Point.fromLngLat(location.longitude,location.latitude)
                locationService.location = point
            }
        }
    }
    @SuppressLint("MissingPermission")
    fun start(locationService: LocationService) {
        this.locationService = locationService
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(locationService)
        val locationRequest = LocationRequest.create().apply {
            interval = LOCATION_UPDATE_INTERVAL
            fastestInterval = LOCATION_UPDATE_FASTEST_INTERVAL
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,null)
    }


}