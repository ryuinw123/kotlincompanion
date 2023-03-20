package com.example.kmitlcompanion.ui.createpolygonevent.helpers

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.presentation.viewmodel.CreatePolygonEventViewModel
import com.example.kmitlcompanion.ui.mapboxview.utils.BitmapUtils
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.Annotation
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.gestures.OnMapClickListener
import com.mapbox.maps.plugin.gestures.OnMapLongClickListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.addOnMapLongClickListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.locationcomponent.location2
import com.mapbox.maps.plugin.scalebar.scalebar
import java.lang.ref.WeakReference
import javax.inject.Inject

class ViewMap @Inject constructor(
    private val bitmapUtils: BitmapUtils
) : DefaultLifecycleObserver , OnMapLongClickListener , OnPointAnnotationDragListener{
    private lateinit var viewModel: CreatePolygonEventViewModel


    private lateinit var polygonAnnotationManager : PolygonAnnotationManager
    private lateinit var pointAnnotationManager: PointAnnotationManager

    private var weakMapView: WeakReference<MapView?>? = null

    fun setup(viewModel: CreatePolygonEventViewModel,mapView: MapView? , callback: (MapboxMap) -> Unit) {

        this.viewModel = viewModel
        weakMapView = WeakReference(mapView)

        mapView?.compass?.enabled = false
        mapView?.scalebar?.enabled = false
        mapView?.location?.enabled = true

        mapView?.getMapboxMap()?.loadStyleUri(
            Style.MAPBOX_STREETS,
        ) {
            callback(mapView.getMapboxMap())
        }


        this.polygonAnnotationManager = mapView?.annotations?.createPolygonAnnotationManager()!!
        this.pointAnnotationManager = mapView?.annotations?.createPointAnnotationManager()!!

        pointAnnotationManager.addDragListener(this)

        pointAnnotationManager.addClickListener(OnPointAnnotationClickListener { annotation ->
            val activePoint = viewModel.activePoint.value
            activePoint?.let {
                it.remove(annotation)
                viewModel.updateActivePoint(it)
            }
            pointAnnotationManager.delete(annotation)
            true
        })

        mapView?.getMapboxMap()?.addOnMapLongClickListener(this)


    }

    fun setCamera(point: Point,zoom : Double) {
        weakMapView?.get()?.getMapboxMap()?.setCamera(CameraOptions.Builder().center(point)
            .zoom(zoom)
            .build())
    }

    override fun onMapLongClick(point: Point): Boolean {
        addPoint(point)
        redraw()
        return true
    }

    fun redraw() {
        polygonAnnotationManager?.deleteAll()

        val activePoint = viewModel.activePoint.value

        activePoint?.let { mapboxPoint ->
            if (mapboxPoint.size <= 3)
                return

            val points = mutableListOf<Point>()

            mapboxPoint.map {
                points.add(it.point)
            }



            val polygonAnnotationOptions: PolygonAnnotationOptions = PolygonAnnotationOptions()
                .withPoints(listOf(points))
                // Style the polygon that will be added to the map.
                .withFillColor("#ee4e8b")
                .withFillOpacity(0.4)

            polygonAnnotationManager?.create(polygonAnnotationOptions)
        }


    }

    private fun addPoint(point: Point) {
        bitmapUtils.bitmapFromDrawableRes(
            R.drawable.type_pin
        )?.let { it ->
            val pointAnnotationOptions = PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(it)
                .withDraggable(true)
            val pointAnnotation = pointAnnotationManager.create(pointAnnotationOptions)
            val activePoint = viewModel.activePoint.value ?: mutableListOf()

            activePoint.add(pointAnnotation)
            viewModel.updateActivePoint(activePoint)
        }

    }

    override fun onAnnotationDrag(annotation: Annotation<*>) {
        redraw()
    }

    override fun onAnnotationDragFinished(annotation: Annotation<*>) {
    }

    override fun onAnnotationDragStarted(annotation: Annotation<*>) {
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