package com.example.kmitlcompanion.ui.createcircleevent.helpers

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.presentation.viewmodel.CreateCircleEventViewModel
import com.example.kmitlcompanion.ui.mapboxview.utils.BitmapUtils
import com.example.kmitlcompanion.ui.mapboxview.utils.MapperUtils
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.Annotation
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.gestures.OnMapLongClickListener
import com.mapbox.maps.plugin.gestures.addOnMapLongClickListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.scalebar.scalebar
import java.lang.ref.WeakReference
import javax.inject.Inject

class ViewMap @Inject constructor(
    private val bitmapUtils: BitmapUtils ,
    private val mapperUtils: MapperUtils
) : DefaultLifecycleObserver, OnMapLongClickListener {
    private lateinit var viewModel: CreateCircleEventViewModel


    private lateinit var pointAnnotationManager: PointAnnotationManager
    private lateinit var polygonAnnotationManager: PolygonAnnotationManager


    private var weakMapView: WeakReference<MapView?>? = null

    fun setup(
        viewModel: CreateCircleEventViewModel,
        mapView: MapView?,
        callback: (MapboxMap) -> Unit
    ) {

        this.viewModel = viewModel
        weakMapView = WeakReference(mapView)

        mapView?.compass?.enabled = false
        mapView?.scalebar?.enabled = false
        mapView?.location?.enabled = false

        mapView?.getMapboxMap()?.loadStyleUri(
            Style.MAPBOX_STREETS,
        ) {
            callback(mapView.getMapboxMap())
        }

        this.pointAnnotationManager = mapView?.annotations?.createPointAnnotationManager()!!
        this.polygonAnnotationManager = mapView?.annotations?.createPolygonAnnotationManager()!!

        this.pointAnnotationManager.addDragListener(object : OnPointAnnotationDragListener {
            override fun onAnnotationDrag(annotation: Annotation<*>) {
                viewModel.updateCurrentPin(annotation as PointAnnotation)
            }

            override fun onAnnotationDragFinished(annotation: Annotation<*>) {
            }

            override fun onAnnotationDragStarted(annotation: Annotation<*>) {
            }

        })

        mapView?.getMapboxMap()?.addOnMapLongClickListener(this)




    }

    fun setCamera(point: Point) {
        weakMapView?.get()?.getMapboxMap()?.setCamera(CameraOptions.Builder().center(point).build())
    }

    override fun onMapLongClick(point: Point): Boolean {
        deletePoint()
        addPoint(point)
        return true
    }

    fun redraw() {
        val point = viewModel.currentPin.value
        point?.let {
            polygonAnnotationManager.deleteAll()
            val radius = viewModel.currentRadius.value ?: 0.1
            Log.d("CreateEvent" , "radius = $radius")
            val points = mapperUtils.createCircleCoordinates(it.point!!, radius)
            viewModel.updateCurrentPolygon(points)
            val mapboxPoints = mapperUtils.doublePolygonToPoint(points)
            val polygonAnnotationOptions: PolygonAnnotationOptions = PolygonAnnotationOptions()
                .withPoints(mapboxPoints)
                // Style the polygon that will be added to the map.
                .withFillColor("#ee4e8b")
                .withFillOpacity(0.4)
            polygonAnnotationManager.create(polygonAnnotationOptions)
        }


    }

    private fun deletePoint() {
        pointAnnotationManager.deleteAll()
    }

    private fun addPoint(point: Point) {
        bitmapUtils.bitmapFromDrawableRes(
            R.drawable.red_marker
        )?.let {
            val pointAnnotationOptions = PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(it)
                .withDraggable(true)
            val pointAnnotation = pointAnnotationManager.create(pointAnnotationOptions)
            viewModel.updateCurrentPin(pointAnnotation)
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