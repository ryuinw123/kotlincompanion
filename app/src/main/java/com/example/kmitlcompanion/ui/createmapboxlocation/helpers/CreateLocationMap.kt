package com.example.kmitlcompanion.ui.createmapboxlocation.helpers

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.extension.observable.eventdata.CameraChangedEventData
import com.mapbox.maps.plugin.delegates.listeners.OnCameraChangeListener
import java.lang.ref.WeakReference
import javax.inject.Inject

internal class CreateLocationMap @Inject constructor() : DefaultLifecycleObserver {

    private var weakMapView: WeakReference<MapView?>? = null

    fun setup(mapView: MapView? , callback: (MapboxMap) -> Unit) {


        weakMapView = WeakReference(mapView)


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