package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.domain.model.MapInformation
import com.example.kmitlcompanion.ui.mapboxview.converter.BitmapConverter
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import java.lang.ref.WeakReference
import javax.inject.Inject


internal class ViewMap @Inject constructor(
) : DefaultLifecycleObserver {
    @Inject lateinit var bitmapConverter : BitmapConverter


    private var weakMapView: WeakReference<MapView?>? = null

    fun setup(mapView: MapView? , callback: (MapboxMap) -> Unit) {
        weakMapView = WeakReference(mapView)

        mapView?.getMapboxMap()?.loadStyleUri(
            Style.MAPBOX_STREETS,
        ) {
            callback(mapView.getMapboxMap())
        }
    }

    fun updateMap(
        context : Context,
        information: MapInformation
    )
    {
        addAnnotationToMap(context,information)
    }

    private fun addAnnotationToMap(context : Context, information : MapInformation) {
        bitmapConverter.bitmapFromDrawableRes(
            context,
            R.drawable.red_marker
        )?.let {
            val annotationApi = weakMapView?.get()?.annotations
            val pointAnnotationManager = annotationApi?.createPointAnnotationManager()

            information.mapPoints.map { point ->
                val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                    .withPoint(Point.fromLngLat(point.longitude, point.latitude))
                    .withIconImage(it)
                pointAnnotationManager?.create(pointAnnotationOptions)
            }

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