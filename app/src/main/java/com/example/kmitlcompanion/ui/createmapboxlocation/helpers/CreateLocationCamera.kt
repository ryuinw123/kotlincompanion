package com.example.kmitlcompanion.ui.createmapboxlocation.helpers

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.kmitlcompanion.presentation.viewmodel.CreateMapboxLocationViewModel
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.extension.observable.eventdata.CameraChangedEventData
import com.mapbox.maps.plugin.delegates.listeners.OnCameraChangeListener
import java.lang.ref.WeakReference
import javax.inject.Inject


internal class CreateLocationCamera @Inject constructor(
) : OnCameraChangeListener, DefaultLifecycleObserver {
    private lateinit var viewModel: CreateMapboxLocationViewModel
    private var weakMapboxMap: WeakReference<MapboxMap?>? = null
    fun setup(mapboxMap: MapboxMap? , viewModel: CreateMapboxLocationViewModel) {
        weakMapboxMap = WeakReference(mapboxMap)
        addListener(mapboxMap)
        this.viewModel = viewModel
    }
    private fun addListener(mapboxMap: MapboxMap?) {
        mapboxMap?.addOnCameraChangeListener(this)
    }
    override fun onCameraChanged(eventData: CameraChangedEventData) {
        val center = weakMapboxMap?.get()?.cameraState?.center
        viewModel.updateCurrentMapLocation(center)
    }


    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        weakMapboxMap?.get()?.removeOnCameraChangeListener(this)
    }


}