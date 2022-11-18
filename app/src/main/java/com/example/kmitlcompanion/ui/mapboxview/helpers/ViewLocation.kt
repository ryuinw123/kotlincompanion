package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.content.Context
import androidx.core.view.get
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.camera
import java.lang.ref.WeakReference
import javax.inject.Inject

class ViewLocation @Inject constructor(
){


    private var weakContext: WeakReference<Context?>? = null
    private var flyToLocation = false

    fun enableLocationComponent(
        context: Context,
        mapView: MapView,
        flyToLocation: Boolean
    ) {
        weakContext = WeakReference(context)

        mapView.getMapboxMap()?.let {
            if (PermissionsManager.areLocationPermissionsGranted(context)) {
                println("Location Permission Grant")
                onLocationPermissionGranted(
                    mapView = mapView,
                    context = this@ViewLocation.context,
                    flyToLocation = flyToLocation
                )
            }
        }
    }
    fun onLocationPermissionGranted(
        mapView: MapView,
        context: Context?,
        flyToLocation: Boolean
    )
    {



    }



    private val context
        get() = weakContext?.get()
}