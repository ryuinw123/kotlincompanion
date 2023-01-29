package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.background.LocationService
import com.example.kmitlcompanion.background.WorkManagerUtils
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.example.kmitlcompanion.ui.mapboxview.utils.LocationPermissionHelper
import com.example.kmitlcompanion.ui.mapboxview.utils.ToasterUtil
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.maps.*
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import java.lang.ref.WeakReference
import javax.inject.Inject


class ViewLocation @Inject constructor(
    private val toaster: ToasterUtil,
    private val locationPermissionHelper: LocationPermissionHelper
) : DefaultLifecycleObserver {

    private lateinit var viewModel : MapboxViewModel
    private lateinit var weakMapView : WeakReference<MapView?>

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        viewModel.updateUserLocation(it)
    }

    /*private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        mapView?.getMapboxMap()?.setCamera(CameraOptions.Builder().bearing(it).build())
    }*/


    /*private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {}
    }*/


    fun setup(viewModel: MapboxViewModel ,context: Context, mapView : MapView)  {
        this.viewModel = viewModel
        this.weakMapView = WeakReference(mapView)
        locationPermissionHelper.checkPermissions {
            onMapReady(context)
            viewModel.updatePermission(true)
        }
    }

    private fun onMapReady(context: Context){
        initLocationComponent(context)
        //setupGesturesListener()
    }

    /*private fun setupGesturesListener() {
        mapView?.gestures?.addOnMoveListener(onMoveListener)
    }*/

    fun updatePuckIcon(context: Context, icon : String) {
        val locationComponentPlugin = mapView?.location
        if (icon == MODE_NORMAL) {
            locationComponentPlugin?.updateSettings {
                this.locationPuck = LocationPuck2D(
                    topImage = ContextCompat.getDrawable(
                        context,
                        R.drawable.mapbox_user_icon
                    ),
                    bearingImage = ContextCompat.getDrawable(
                        context,
                        R.drawable.mapbox_user_bearing_icon,
                    ),
                    shadowImage = ContextCompat.getDrawable(
                        context,
                        R.drawable.mapbox_user_stroke_icon,
                    )
                )
            }
        }
        else if (icon == MODE_NAVIGATION) {
            locationComponentPlugin?.updateSettings {
                this.locationPuck = LocationPuck2D(
                    bearingImage = ContextCompat.getDrawable(
                        context,
                        R.drawable.mapbox_user_puck_icon,
                    ),
                    shadowImage = ContextCompat.getDrawable(
                        context,
                        R.drawable.mapbox_user_stroke_icon,
                    )
                )
            }
        }
    }

    private fun initLocationComponent(context: Context){
        val locationComponentPlugin = mapView?.location

        locationComponentPlugin?.updateSettings {
            this.enabled = true

            this.locationPuck = LocationPuck2D(
                topImage = ContextCompat.getDrawable(
                    context,
                    R.drawable.mapbox_user_icon
                ),
                bearingImage = ContextCompat.getDrawable(
                    context,
                    R.drawable.mapbox_user_bearing_icon,
                ),
                shadowImage = ContextCompat.getDrawable(
                    context,
                    R.drawable.mapbox_user_stroke_icon,
                ),
                scaleExpression = interpolate {
                    linear()
                    zoom()
                    stop {
                        literal(0.0)
                        literal(0.6)
                    }
                    stop {
                        literal(20.0)
                        literal(1.0)
                    }
                }.toJson()
            )
        }
        locationComponentPlugin?.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        //locationComponentPlugin?.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
    }

    /*private fun onCameraTrackingDismissed() {
        toaster.showToast("onCameraTrackingDismissed", Toast.LENGTH_SHORT)
        mapView?.location?.removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        //mapView?.location?.removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        //mapView?.gestures?.removeOnMoveListener(onMoveListener)
    }*/

    private val mapView
        get() = weakMapView?.get()

    companion object {
        val MODE_NAVIGATION = "Navigation"
        val MODE_NORMAL = "Map"
    }
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        mapView?.location?.removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        //mapView?.location?.removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        //mapView?.gestures?.removeOnMoveListener(onMoveListener)
    }
}