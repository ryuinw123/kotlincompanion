package com.example.kmitlcompanion.ui.createmapboxlocation.helpers

import android.view.View
import androidx.core.view.get
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.extension.observable.eventdata.CameraChangedEventData
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.delegates.listeners.OnCameraChangeListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.scalebar.scalebar
import java.lang.ref.WeakReference
import javax.inject.Inject

internal class CreateLocationMap @Inject constructor() : DefaultLifecycleObserver {

    private var weakMapView: WeakReference<MapView?>? = null

    fun setup(mapView: MapView? , callback: (MapboxMap) -> Unit) {


        weakMapView = WeakReference(mapView)
        mapView?.compass?.enabled = false
        mapView?.scalebar?.enabled = false
        mapView?.location?.enabled = true

        //mapView?.gestures?.rotateEnabled = false



        mapView?.getMapboxMap()?.loadStyleUri(
            Style.MAPBOX_STREETS,
        ) {
            callback(mapView.getMapboxMap())
        }
    }





    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        weakMapView?.get()?.onStop()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        weakMapView?.get()?.onDestroy()
    }


    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        weakMapView?.get()?.onStart()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        weakMapView?.get()?.onStop()
    }


}