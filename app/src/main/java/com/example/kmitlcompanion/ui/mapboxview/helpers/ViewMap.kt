package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.util.Log
import androidx.lifecycle.*
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.domain.model.EventInformation
import com.example.kmitlcompanion.domain.model.MapInformation
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.example.kmitlcompanion.ui.mapboxview.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.geojson.Feature
import com.mapbox.geojson.MultiPoint
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.style.image.image
import com.mapbox.maps.extension.style.layers.addLayer
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
import com.mapbox.turf.TurfMeasurement
import java.lang.ref.WeakReference
import javax.inject.Inject


class ViewMap @Inject constructor(
    private val mapper: MapperUtils,
    private val bitmapConverter: BitmapUtils,
    private val mapExpressionUtils: MapExpressionUtils,
    private val toasterUtil: ToasterUtil,
    //private val timeCounterUtils: TimeCounterUtils,
    //private val timeCounterUtilsOnFinishListener: TimeCounterUtils.OnFinishListener,
    //private val timeCounterUtilsOnTickListener: TimeCounterUtils.OnTickListener,
): DefaultLifecycleObserver,TimeCounterUtils.OnTickListener, TimeCounterUtils.OnFinishListener {

    private lateinit var viewModel: MapboxViewModel

    private var weakMapView: WeakReference<MapView?>? = null

    fun setup(viewModel: MapboxViewModel, mapView: MapView?, callback: (MapboxMap) -> Unit) {

        this.viewModel = viewModel
        weakMapView = WeakReference(mapView)

        mapView?.compass?.updateSettings {
            marginTop = 700F
            marginRight = 48F
        }

//        mapView?.scalebar?.updateSettings {
//            marginLeft = 40F
//            marginTop = 350F
//        }

        mapView?.scalebar?.enabled = false

        mapView?.getMapboxMap()?.loadStyle(
            styleExtension = mapExpressionUtils.getImageStyle(STYLE_ID)
        ) {
            callback(mapView.getMapboxMap())
        }
        TimeCounterUtils.stopTimer()
    }

    fun updateMap(
        //context: Context,
        information: MapInformation,
        //locationId: Long
    ) {
        prepareMarkerToMap(information)
        //initialLocation(locationId)
        //ขอปิดไว้ก่อน เนื่องจากเปิดแล้ว พอเปิดแอพแบบที่ไม่ใช่รัน debug locationId จะไม่กลายเป็น -1 แล้วเข้า if ทำให้ระเบิด
        //รันใน api 29 หรือรันโดย debug ฟังก์ชันนี่จะไม่ทำงาน ทำให้ไม่เกิดบัค
    }

    fun updateEvent(
        eventInformation: EventInformation,
        id : Long
    ){
        prepareEventToMap(eventInformation,id)
        initialEvent(id)
    }


    private fun prepareMarkerToMap(information: MapInformation) {

            mapView?.getMapboxMap()?.getStyle {

                it.removeStyleLayer(LOCATION_LAYER_ID)
                it.removeStyleSource(SOURCE_ID)

                it.addSource(
                    geoJsonSource(SOURCE_ID) {
                        featureCollection(
                            mapper.mapToFeatureCollections(information.mapPoints)
                        )
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
            }

        addPointListener()
    }

    private fun prepareEventToMap(eventInformation: EventInformation,id : Long) {

        mapView?.getMapboxMap()?.getStyle {
            it.removeStyleLayer(AREA_LAYER_ID)
            it.removeStyleSource(AREA_ID)

            it.addSource(
                geoJsonSource(AREA_ID) {
                    featureCollection(
                        mapper.mapToAreaFeatureCollections(eventInformation.eventPoints)
                    )
                }
            )

            it.addLayer(
                fillLayer(AREA_LAYER_ID, AREA_ID) {
                    fillColor("#fff000")
                    fillOpacity(0.3)
                }
            )

        }
        addPointListener()
        //addEventListener()
    }

    private fun initialEvent(eventId : Long){
        if (eventId != 0L){
            val eventInformation = viewModel.mapEventResponse.value
            val eventData = eventInformation?.eventPoints?.firstOrNull{ it.id == eventId }
            eventData?.let {
                val pointsList = it.area
                val pointsFeature = Feature.fromGeometry(MultiPoint.fromLngLats(pointsList))
                val center = TurfMeasurement.center(pointsFeature).geometry() as Point
                eventDetail(
                    it.name,
                    it.id.toString(),
                    it.startTime,
                    it.endTime,
                    center,
                    it.description,
                    it.imageLink,
                    it.type.toInt(),
                    it.url.firstOrNull() ?:"",
                )
            }

            if (eventData == null){
                toasterUtil.showToast("อีเวนต์นี้ถูกลบไปแล้ว")
                viewModel.deleteByIdNotiLog(eventId)
            }
        }
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

    fun locationDetail(name : String ,id : String ,place : String ,address : String ,location : Point ,description : String ,imageList : List<String>) {

        viewModel.getDetailsLocationQuery(id)//get marker details

        viewModel.updateIdLocationLabel(id)
        viewModel.updateCurrentLocationGps(location)
        viewModel.updatePositionFlyer(location)

        viewModel.updateNameLocationLabel(name)
        viewModel.updatePlaceLocationLabel(place)
        viewModel.updateAddressLocationLabel(address)
        viewModel.updateDescriptionLocationLabel(description)
        viewModel.updateImageLink(imageList)
        viewModel.setIconLocation(R.drawable.ic_location_on_red_24dp)

        viewModel.changeEventState(false)
        viewModel.updateBottomSheetState(BottomSheetBehavior.STATE_HALF_EXPANDED)

        TimeCounterUtils.stopTimer()
        //timeCounterUtils.timer?.cancel()

    }

    fun eventDetail(name : String ,eventId : String ,startTime : String,endTime : String,
                            location : Point ,description : String ,imageList : List<String>,
        eventType : Int,eventUrl : String) {

        viewModel.getEventDetailsLocationQuery(eventId)//get marker details
        //Log.d("test_event","$name $startTime $endTime $description $imageList")
        viewModel.updateIdLocationLabel(eventId)
        viewModel.updateCurrentLocationGps(location)
        viewModel.updatePositionFlyer(location)

        viewModel.updateNameLocationLabel(name)
        viewModel.setStartTime(startTime)
        viewModel.setEndTime(endTime)
        viewModel.updateDescriptionLocationLabel(description)
        viewModel.updateImageLink(imageList)
        viewModel.setIconLocation(R.drawable.ic_event_48px)

        viewModel.setEventType(eventType)
        viewModel.setEventUrl(eventUrl)

        viewModel.changeEventState(true)
        viewModel.updateBottomSheetState(BottomSheetBehavior.STATE_HALF_EXPANDED)

        TimeCounterUtils.startCountdownTimer(startTime, endTime, this, this)
        //startCountdownTimer(endTime)
        Log.d("test_time",endTime)

    }

    override fun onTick(formattedTime: String) {
        viewModel.changeEventTimerEnd(formattedTime)
    }

    override fun onFinish() {
        viewModel.changeEventTimerEnd("อีเวนต์จบแล้ว")
    }

    private fun addPointListener() {
        val mapboxMap = mapView?.getMapboxMap()

        if (viewModel.mapInformationResponse?.value != null || viewModel.mapEventResponse?.value != null){

        //วอนมาช่วยแก้ฟังก์ชั่นนี้หน่อยครับจนปัญญา

        mapboxMap?.addOnMapClickListener {
            val screenPoint = mapboxMap.pixelForCoordinate(it)
            val queryOptions = RenderedQueryOptions(listOf(LOCATION_LAYER_ID) , null)
            mapboxMap.queryRenderedFeatures(
                RenderedQueryGeometry(screenPoint),queryOptions)   { expect ->
                val queriedFeature: List<QueriedFeature> = expect.value ?: emptyList()

                if (queriedFeature.isNotEmpty()) {

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
                            val startTime = selectedFeature.getStringProperty("startTime")
                            val endTime = selectedFeature.getStringProperty("endTime")
                            val type = selectedFeature.getNumberProperty("type")
                            val url = selectedFeature.getStringProperty("url")

                            eventDetail(
                                name =name,
                                eventId =id,
                                startTime = startTime,
                                endTime = endTime,
                                location = center,
                                description = description,
                                imageList = imageList,
                                eventType = type.toInt(),
                                eventUrl = url,
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

    }

    fun flyMapOnReady(){
        Log.d("test_fly",viewModel.userLocation.value.toString())
        viewModel.userLocation.value?.let { viewModel.updatePositionFlyer(it) }
    }


    private fun addEventListener() {
        val mapboxMap = mapView?.getMapboxMap()

        mapboxMap?.addOnMapClickListener {
            val screenPoint = mapboxMap.pixelForCoordinate(it)
            //val queryOptions = RenderedQueryOptions(listOf(LOCATION_LAYER_ID) , null)
            val queryOptions = RenderedQueryOptions(listOf(AREA_LAYER_ID) , null)
            //Log.d("Geofence" , "QueryOption 1")

//            mapboxMap.queryRenderedFeatures(
//                RenderedQueryGeometry(screenPoint),queryOptions)   { expect ->
//                val queriedFeature: List<QueriedFeature> = expect.value ?: emptyList()
//
//                    //val queryOptions = RenderedQueryOptions(listOf(AREA_LAYER_ID) , null)
//                    //Log.d("Geofence" , "QueryOption Step 1")

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
                //}
            }
            true
        }
    }

//    fun startCountdownTimer(targetTime: String) {
//        val targetDateTime = timeCounterUtils.timeFormat.parse(targetTime)
//        timeCounterUtils.timer?.cancel()
//        timeCounterUtils.timer = object : CountDownTimer(targetDateTime.time - System.currentTimeMillis(), timeCounterUtils.COUNTDOWNINTERVAL) {
//            override fun onTick(millisUntilFinished: Long) {
//                val result = timeCounterUtils.getFormattedTime(millisUntilFinished)
//                viewModel.changeEventTimerEnd(result)
//            }
//
//            override fun onFinish() {
//                viewModel.changeEventTimerEnd("อีเวนต์จบแล้ว")
//            }
//        }.start()
//    }


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

                    eventDetail(
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
                bearing(0.0)
                pitch(viewModel.pitchFlyer.value)
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

    fun flyToPitch(pitch : Double){
        mapView?.getMapboxMap()?.flyTo(
            cameraOptions {
                pitch(pitch)
            },
            MapAnimationOptions.mapAnimationOptions {
                duration(2000)
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
        const val TOILET = "toilet"
        const val BANK = "bank"
    }


}