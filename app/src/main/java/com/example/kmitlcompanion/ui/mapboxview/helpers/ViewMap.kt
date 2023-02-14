package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.domain.model.MapInformation
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.example.kmitlcompanion.ui.mapboxview.utils.BitmapUtils
import com.example.kmitlcompanion.ui.mapboxview.utils.MapExpressionUtils
import com.example.kmitlcompanion.ui.mapboxview.utils.MapperUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.maps.*
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.style.image.image
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.SymbolLayerDsl
import com.mapbox.maps.extension.style.layers.generated.fillLayer
import com.mapbox.maps.extension.style.layers.generated.symbolLayer
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.scalebar.scalebar
import com.mapbox.maps.plugin.gestures.removeOnMapClickListener
import com.mapbox.turf.TurfJoins
import com.mapbox.turf.TurfMeasurement
import java.lang.ref.WeakReference
import javax.inject.Inject


class ViewMap @Inject constructor(
    private val mapper: MapperUtils,
    private val bitmapConverter: BitmapUtils,
    private val mapExpressionUtils: MapExpressionUtils,
) : DefaultLifecycleObserver {

    private lateinit var viewModel: MapboxViewModel

    private var weakMapView: WeakReference<MapView?>? = null


    fun setup(viewModel: MapboxViewModel, mapView: MapView?, callback: (MapboxMap) -> Unit) {

        this.viewModel = viewModel
        weakMapView = WeakReference(mapView)

        mapView?.compass?.updateSettings {
            marginTop = 350F
            marginRight = 40F
        }

        mapView?.scalebar?.updateSettings {
            marginLeft = 40F
            marginTop = 350F
        }

        mapView?.getMapboxMap()?.loadStyle(
            styleExtension = style(styleUri = STYLE_ID) {
                +image(LOCATION) {
                    var bitmap = bitmapConverter.bitmapFromDrawableRes(R.drawable.type_pin)!!
//                    var bitmapImage = Bitmap.createScaledBitmap(bitmap
//                        ,bitmap.width * 0.5 as Int
//                        , bitmap.width * 0.5 as Int, true)
                    bitmap(bitmap)
                }
                +image(RESTAURANT) {
                    bitmap(bitmapConverter.bitmapFromDrawableRes(R.drawable.type_restaurant)!!)
                }
                +image(SCHOOL) {
                    bitmap(bitmapConverter.bitmapFromDrawableRes(R.drawable.type_school)!!)
                }
                +image(ROOM) {
                    bitmap(bitmapConverter.bitmapFromDrawableRes(R.drawable.type_room)!!)
                }
                +image(SHOP) {
                    bitmap(bitmapConverter.bitmapFromDrawableRes(R.drawable.type_shop)!!)
                }
                +image(BUILDING) {
                    bitmap(bitmapConverter.bitmapFromDrawableRes(R.drawable.type_building)!!)
                }
                +image(DORM) {
                    bitmap(bitmapConverter.bitmapFromDrawableRes(R.drawable.type_dorm)!!)
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
        prepareMarkerToMap(information)
        //initialLocation(locationId)
        //ขอปิดไว้ก่อน เนื่องจากเปิดแล้ว พอเปิดแอพแบบที่ไม่ใช่รัน debug locationId จะไม่กลายเป็น -1 แล้วเข้า if ทำให้ระเบิด
        //รันใน api 29 หรือรันโดย debug ฟังก์ชันนี่จะไม่ทำงาน ทำให้ไม่เกิดบัค
    }


    private fun prepareMarkerToMap(information: MapInformation) {

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
                            mapper.mapToAreaFeatureCollections(viewModel.mapEventResponse.value!!.eventPoints)
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

                        iconImage(mapExpressionUtils.getImageExpression())

                        iconAllowOverlap(true)
                        iconAnchor(IconAnchor.BOTTOM)
                    }
                )
                //it.setStyleLayerProperty(LOCATION_LAYER_ID,"visibility", Value.valueOf("none"))
            }
        //}

        addPointListener()
        //addEventListener()
    }

    private fun initialLocation(locationId : Long) {
        Log.d("test_locationId",locationId.toString())
        if (locationId != -1L) {
            /*val kmitlPoint = Point.fromLngLat(100.7811,13.7310)
            viewModel.updatePositionFlyer(kmitlPoint)*/
            Log.d("test_locationIdinif",locationId.toString())
            val mapInformation = viewModel.mapInformationResponse.value
            val locationList = mapInformation?.mapPoints

            val locationDetail = locationList?.single { location ->
                location.id == locationId
            }

            locationDetail?.let {
                Log.d("Selected Feature" , "tod sob")
                val locationPoint = Point.fromLngLat(it.longitude,it.latitude)

                locationDetail(
                    name=it.name,
                    id=it.id.toString(),
                    place= it.place,
                    address = it.address,
                    location = locationPoint,
                    description = it.description,
                    imageList = it.imageLink
                )
            }
        }
    }

    private fun locationDetail(name : String ,id : String ,place : String ,address : String ,location : Point ,description : String ,imageList : List<String>) {

        viewModel.getDetailsLocationQuery(id)//get marker details

        viewModel.updateIdLocationLabel(id)
        viewModel.updateCurrentLocationGps(location)
        viewModel.updatePositionFlyer(location)

        viewModel.updateNameLocationLabel(name)
        viewModel.updatePlaceLocationLabel(place)
        viewModel.updateAddressLocationLabel(address)
        viewModel.updateDescriptionLocationLabel(description)
        viewModel.updateImageLink(imageList)

        viewModel.updateBottomSheetState(BottomSheetBehavior.STATE_HALF_EXPANDED)


    }

    private fun addPointListener() {
        val mapboxMap = mapView?.getMapboxMap()

        mapboxMap?.addOnMapClickListener {
            val screenPoint = mapboxMap.pixelForCoordinate(it)
            val queryOptions = RenderedQueryOptions(listOf(LOCATION_LAYER_ID) , null)

            //Log.d("Geofence" , "QueryOption 1")

            mapboxMap.queryRenderedFeatures(
                RenderedQueryGeometry(screenPoint),queryOptions)   { expect ->
                val queriedFeature: List<QueriedFeature> = expect.value ?: emptyList()

                if (queriedFeature.isNotEmpty()) {

                    //Log.d("Geofence" , "QueryOption 2 = $queriedFeature")

                    val selectedFeature = queriedFeature[0].feature
                    //Log.d("Selected Feature" , selectedFeature.toString())
                    val point = selectedFeature.geometry() as Point
                    val name = selectedFeature.getStringProperty("name")
                    val place = selectedFeature.getStringProperty("place")
                    val address = selectedFeature.getStringProperty("address")
                    val description = selectedFeature.getStringProperty("description")
                    val id = selectedFeature.getStringProperty("id")
                    val imageList = selectedFeature.getStringProperty("imageLink").removePrefix("[").removeSuffix("]").split(",").map { it.trim() }.toList()

                    locationDetail(
                        name=name,
                        id=id,
                        place= place,
                        address = address,
                        location = point,
                        description = description,
                        imageList = imageList
                    )

                }
                else{
                    val queryOptions = RenderedQueryOptions(listOf(AREA_LAYER_ID) , null)
                    //Log.d("Geofence" , "QueryOption Step 1")

                    mapboxMap.queryRenderedFeatures(
                        RenderedQueryGeometry(screenPoint),queryOptions)   { expect ->
                        val queriedFeature: List<QueriedFeature> = expect.value ?: emptyList()
                        if (queriedFeature.isNotEmpty()) {
                            //Log.d("Geofence" , "QueryOptionArea 2 = $queriedFeature")
                            //isFound = true
                            val selectedFeature = queriedFeature[0].feature
                            val center = TurfMeasurement.center(selectedFeature).geometry() as Point
                            val name = selectedFeature.getStringProperty("name")
                            val description = selectedFeature.getStringProperty("description")
                            val id = selectedFeature.getStringProperty("id")
                            val imageList = selectedFeature.getStringProperty("imageLink").removePrefix("[").removeSuffix("]").split(",").map { it.trim() }.toList()

                            locationDetail(
                                name=name,
                                id=id,
                                place= "",
                                address = "",
                                location = center,
                                description = description,
                                imageList = imageList
                            )

                        }
                        else{
                            hiddenBottomSheetState()
                        }
                    }
                }
            }



            true
        }

    }

    /*private fun addEventListener() {
        val mapboxMap = mapView?.getMapboxMap()
        //var isFound = false

        mapboxMap?.addOnMapClickListener {
            val screenPoint = mapboxMap.pixelForCoordinate(it)
            val queryOptions = RenderedQueryOptions(listOf(AREA_LAYER_ID) , null)
            Log.d("Geofence" , "QueryOption Step 1")

            mapboxMap.queryRenderedFeatures(
                RenderedQueryGeometry(screenPoint),queryOptions)   { expect ->
                val queriedFeature: List<QueriedFeature> = expect.value ?: emptyList()
                if (queriedFeature.isNotEmpty()) {
                    Log.d("Geofence" , "QueryOptionArea 2 = $queriedFeature")
                    //isFound = true
                    val selectedFeature = queriedFeature[0].feature
                    Log.d("Selected Feature" , selectedFeature.toString())
                    val name = selectedFeature.getStringProperty("name")
                    val description = selectedFeature.getStringProperty("description")
                    val id = selectedFeature.getStringProperty("id")
                    val imageList = selectedFeature.getStringProperty("imageLink").removePrefix("[").removeSuffix("]").split(",").map { it.trim() }.toList()

                    locationDetail(
                        name=name,
                        id=id,
                        place= "",
                        address = "",
                        location = it,
                        description = description,
                        imageList = imageList
                    )

                }
                else{
                    hiddenBottomSheetState()
                }
            }
            true
        }
    }*/



    private fun hiddenBottomSheetState() {
        viewModel.updateBottomSheetState(BottomSheetBehavior.STATE_HIDDEN)

    }

    fun flyToLocation(point: Point) {
        mapView?.getMapboxMap()?.flyTo(
            cameraOptions {

                //center(Point.fromLngLat(point.longitude(), point.latitude() + 0.01))
                center(Point.fromLngLat(point.longitude(), point.latitude()))
                zoom(18.0)
                bearing(180.0)
                pitch(50.0)
            },
            MapAnimationOptions.mapAnimationOptions {
                duration(2000)
            }
        )
    }

    fun flyToLocation(point: Point, zoom : Double, bearing : Double, pitch : Double, duration : Long) {
        mapView?.getMapboxMap()?.flyTo(
            cameraOptions {
                //center(Point.fromLngLat(point.longitude(), point.latitude() + 0.01))
                center(Point.fromLngLat(point.longitude(), point.latitude()))
                zoom(zoom)
                bearing(bearing)
                pitch(pitch)
            },
            MapAnimationOptions.mapAnimationOptions {
                duration(duration)
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

        const val LOCATION = "locations"

        //tag name
        const val TAG = "tag"

        //tag icon
        const val RESTAURANT = "restaurant"
        const val SCHOOL = "school"
        const val ROOM = "room"
        const val SHOP = "shop"
        const val BUILDING = "building"
        const val DORM = "dorm"
    }


}