package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.content.Context
import androidx.lifecycle.*
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.domain.model.ActivePoint
import com.example.kmitlcompanion.domain.model.MapInformation
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.example.kmitlcompanion.ui.mapboxview.utils.BitmapUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import java.lang.ref.WeakReference
import javax.inject.Inject


internal class ViewMap @Inject constructor(
) : DefaultLifecycleObserver {
    @Inject lateinit var bitmapConverter : BitmapUtils

    private lateinit var viewModel : MapboxViewModel

    private var weakMapView: WeakReference<MapView?>? = null

    fun setup(viewModel: MapboxViewModel, mapView: MapView?, callback: (MapboxMap) -> Unit) {

        this.viewModel = viewModel
        weakMapView = WeakReference(mapView)


        mapView?.getMapboxMap()?.loadStyleUri(
            "mapbox://styles/ryuinw123/cla16mlr3006715p56830t6xv",
        ) {
            callback(mapView.getMapboxMap())
        }
    }

    fun updateMap(
        context : Context,
        information: MapInformation
    )
    {
        prepareAnnotationToMap(context,information)
        addOnclickMapEvent()
    }

    private fun prepareAnnotationToMap(context : Context, information : MapInformation) {
        val annotationApi = weakMapView?.get()?.annotations
        val pointAnnotationManager = annotationApi?.createPointAnnotationManager()
        val pointList : MutableList<ActivePoint> = mutableListOf()
        bitmapConverter.bitmapFromDrawableRes(
            context,
            R.drawable.locationpin_48px
        )?.let {
            information.mapPoints.map { point ->
                val pointAnnotationOptions = PointAnnotationOptions()
                    .withPoint(Point.fromLngLat(point.longitude, point.latitude))
                    .withIconImage(it)
                val pointAnnotation = pointAnnotationManager?.create(pointAnnotationOptions)
                pointList.add(
                    ActivePoint(
                        pointAnnotation = pointAnnotation!!,
                        mapPoint = point)
                )
            }

        }

        addPointListener(pointList,pointAnnotationManager!!)
    }

    private fun addPointListener(activePointList: List<ActivePoint> , pointAnnotationManager: PointAnnotationManager) {
        var isClick = false
        pointAnnotationManager?.addClickListener { clickedAnnotation ->
            activePointList.map {
                if (it.pointAnnotation == clickedAnnotation) {
                    val point = Point.fromLngLat(it.mapPoint.longitude,it.mapPoint.latitude)
                    viewModel.updateIdLocationLabel(it.mapPoint.id.toString())
                    viewModel.updateNameLocationLabel(it.mapPoint.place)
                    viewModel.updateDescriptionLocationLabel(it.mapPoint.description)
                    viewModel.updateCurrentLocationGps(point)
                    viewModel.updatePositionFlyer(point)
                    viewModel.updateBottomSheetState(BottomSheetBehavior.STATE_HALF_EXPANDED)
                    viewModel.updateImageLink(it.mapPoint.imageLink)
                    isClick = true
                }
            }
            isClick
        }
    }

    private fun addOnclickMapEvent() {
        weakMapView?.get()?.getMapboxMap()?.addOnMapClickListener {
            println("Hello Drop")
            viewModel.updateBottomSheetState(BottomSheetBehavior.STATE_HIDDEN)
            true
        }

    }

    fun flyToLocation(point: Point) {
        weakMapView?.get()?.getMapboxMap()?.flyTo(
            cameraOptions {

                center(Point.fromLngLat(point.longitude(),point.latitude()+0.01))
                zoom(12.0)
                bearing(180.0)
                pitch(50.0)
            },
            MapAnimationOptions.mapAnimationOptions {
                duration(3000)
            }
        )
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