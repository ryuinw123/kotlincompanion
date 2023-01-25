package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import androidx.core.view.get
import androidx.lifecycle.*
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.domain.model.ActivePoint
import com.example.kmitlcompanion.domain.model.MapInformation
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.example.kmitlcompanion.ui.mapboxview.utils.BitmapUtils
import com.example.kmitlcompanion.ui.mapboxview.utils.MapperUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.GeoJson
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.style.expressions.dsl.generated.boolean
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.extension.style.expressions.dsl.generated.literal
import com.mapbox.maps.extension.style.expressions.generated.Expression.Companion.exponential
import com.mapbox.maps.extension.style.image.image
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.CircleLayer
import com.mapbox.maps.extension.style.layers.generated.circleLayer
import com.mapbox.maps.extension.style.layers.generated.fillLayer
import com.mapbox.maps.extension.style.layers.generated.symbolLayer
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.sources.getSource
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import java.lang.ref.WeakReference
import javax.inject.Inject
import kotlin.math.exp


internal class ViewMap @Inject constructor(
    private val mapper: MapperUtils,
    private val bitmapConverter: BitmapUtils,
) : DefaultLifecycleObserver {

    private lateinit var viewModel: MapboxViewModel

    private var weakMapView: WeakReference<MapView?>? = null


    fun setup(viewModel: MapboxViewModel, mapView: MapView?, callback: (MapboxMap) -> Unit) {

        this.viewModel = viewModel
        weakMapView = WeakReference(mapView)

        mapView?.getMapboxMap()?.loadStyle(
            styleExtension = style(styleUri = STYLE_ID) {
                +image(LOCATION) {
                    bitmap(bitmapConverter.bitmapFromDrawableRes(R.drawable.red_marker)!!)
                }
            }
        ) {
            callback(mapView.getMapboxMap())
        }


    }

    fun updateMap(
        context: Context,
        information: MapInformation,
        locationId: Long
    ) {
        prepareMarkerToMap(context, information)
        initialLocation(locationId)
    }

    private fun prepareMarkerToMap(context: Context, information: MapInformation) {

        //mapView?.getMapboxMap()?.getStyle()?.let {
            /* in testing */
            mapView?.getMapboxMap()?.getStyle {
                //Log.d("add Source", information.mapPoints.toString().length.toString())
                it.removeStyleLayer(AREA_LAYER_ID)
                it.removeStyleLayer(LOCATION_LAYER_ID)
                it.removeStyleSource(SOURCE_ID)
                it.removeStyleSource(AREA_ID)
                //Log.d("add Source","Add")

                it.addSource(
                    geoJsonSource(SOURCE_ID) {
                        featureCollection(
                            mapper.mapToFeatureCollections(information.mapPoints)
                        )
                    }
                )

                it.addSource(
                    geoJsonSource(AREA_ID) {
                        featureCollection(
                            mapper.mapToCircleFeatureCollections(information.mapPoints)
                        )
                    }
                )

                it.addLayer(
                    fillLayer(AREA_LAYER_ID, AREA_ID) {
                        fillColor("#fff000")
                        fillOpacity(0.3)
                    }
                )

                it.addLayer(
                    symbolLayer(LOCATION_LAYER_ID, SOURCE_ID) {
                        sourceLayer(SOURCE_LAYER_ID)
                        iconImage(
                            literal(LOCATION)
                        )
                        iconAllowOverlap(true)
                        iconAnchor(IconAnchor.BOTTOM)
                    }
                )
            }
        //}
        addPointListener()
    }

    private fun initialLocation(locationId : Long) {
        if (locationId != -1L) {
            /*val kmitlPoint = Point.fromLngLat(100.7811,13.7310)
            viewModel.updatePositionFlyer(kmitlPoint)*/

            val mapInformation = viewModel.mapInformationResponse.value
            val locationList = mapInformation?.mapPoints

            val locationDetail = locationList?.single { location ->
                location.id == locationId
            }

            locationDetail?.let {

                val locationPoint = Point.fromLngLat(it.longitude,it.latitude)
                locationDetail(it.id.toString() , it.place , it.description , locationPoint)

            }

        }


    }

    private fun locationDetail(id : String , place : String , description : String , location : Point) {
        viewModel.updateIdLocationLabel(id)
        viewModel.updateNameLocationLabel(place)
        viewModel.updateDescriptionLocationLabel(description)
        viewModel.updateCurrentLocationGps(location)
        viewModel.updatePositionFlyer(location)
        viewModel.updateBottomSheetState(BottomSheetBehavior.STATE_HALF_EXPANDED)
    }

    private fun addPointListener() {
        val mapboxMap = mapView?.getMapboxMap()

        //var isFound = false

        mapboxMap?.addOnMapClickListener {
            val screenPoint = mapboxMap.pixelForCoordinate(it)
            val queryOptions = RenderedQueryOptions(listOf(LOCATION_LAYER_ID) , null)

            mapboxMap.queryRenderedFeatures(
                RenderedQueryGeometry(screenPoint),
                queryOptions)   { expect ->
                val queriedFeature: List<QueriedFeature> = expect.value ?: emptyList()
                if (queriedFeature.isNotEmpty()) {
                    //isFound = true
                    val selectedFeature = queriedFeature[0].feature
                    Log.d("Selected Feature" , selectedFeature.toString())
                    val id = selectedFeature.getStringProperty("id")
                    val place = selectedFeature.getStringProperty("place")
                    val description = selectedFeature.getStringProperty("description")
                    val point = selectedFeature.geometry() as Point

                    locationDetail(id , place , description , point)

                }
                else{
                    hiddenBottomSheetState()
                }
            }
            false
        }

        /*var isClick = false
        pointAnnotationManager?.addClickListener { clickedAnnotation ->
            activePointList.map {
                if (it.pointAnnotation == clickedAnnotation) {
                    val point = Point.fromLngLat(it.mapPoint.longitude, it.mapPoint.latitude)
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
        }*/
    }

    private fun hiddenBottomSheetState() {
        viewModel.updateBottomSheetState(BottomSheetBehavior.STATE_HIDDEN)

    }

    fun flyToLocation(point: Point) {
        mapView?.getMapboxMap()?.flyTo(
            cameraOptions {

                center(Point.fromLngLat(point.longitude(), point.latitude() + 0.01))
                zoom(12.0)
                bearing(180.0)
                pitch(50.0)
            },
            MapAnimationOptions.mapAnimationOptions {
                duration(3000)
            }
        )
    }

    private val mapView
        get() = weakMapView?.get()

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


    companion object {
        const val LAYER_MARKER = "LAYER_MARKER"
        const val SOURCE_LAYER_ID = "Yosemite_POI-38jhes"
        const val SOURCE_ID = "SOURCE_ID"
        const val AREA_ID = "AREA_ID"
        const val MARKER_ID = "MARKER_ID"
        const val STYLE_ID = "mapbox://styles/ryuinw123/cla16mlr3006715p56830t6xv"
        const val LOCATION_LAYER_ID = "LOCATION_LAYER_ID"
        const val AREA_LAYER_ID = "AREA_LAYER_ID"


        private const val LOCATION = "locations"

    }


}