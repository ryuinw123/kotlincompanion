package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kmitlcompanion.domain.model.TagDetail
import com.example.kmitlcompanion.domain.model.TagViewDataDetail
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.example.kmitlcompanion.ui.createlocation.utils.TagTypeListUtil
import com.example.kmitlcompanion.ui.mapboxview.adapter.OnTagClickListener
import com.example.kmitlcompanion.ui.mapboxview.adapter.TagAdapter
import com.example.kmitlcompanion.ui.mapboxview.utils.MapExpressionUtils
import com.google.android.material.card.MaterialCardView
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.symbolLayer
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import java.lang.ref.WeakReference
import javax.inject.Inject
import kotlin.math.*


class ViewTag @Inject constructor(
    private val tagAdapter: TagAdapter,
    private val tagTypeListUtil: TagTypeListUtil,
    private val mapExpressionUtils: MapExpressionUtils,
) {

    private lateinit var viewModel: MapboxViewModel
    private var weakMapView: WeakReference<MapView?>? = null
    private var itemClick : MutableList<Int?> = mutableListOf()
    private lateinit var context : Context
    //private lateinit var weakTagRecyclerView: WeakReference<TagRe>

    fun setup(map: MapView, viewModel : MapboxViewModel, tagRecyclerView: RecyclerView, clearTagCardView: MaterialCardView,context : Context) {
        this.viewModel = viewModel
        weakMapView = WeakReference(map)
        this.context = context

        //set init tag item
        viewModel.updateItemTagList(tagTypeListUtil.getAvailableMutableListOfTagTypeDetails().mapIndexed { index, tagDetail ->
                                        TagViewDataDetail(tagDetail, false)
                                    } as MutableList<TagViewDataDetail>
                                    ,TagViewDataDetail(TagDetail(999999,"x",0), false))

        //this.weakTagRecyclerView = WeakReference(tagRecyclerView)
        tagRecyclerView.visibility = View.VISIBLE
        tagRecyclerView.setHasFixedSize(true)
        tagRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        tagRecyclerView.adapter = tagAdapter

        tagAdapter.setOnTagClickListener(object : OnTagClickListener{
            override fun onTagClick(
                tagViewDataDetail: TagViewDataDetail,
                position: Int,
                code: Int
            ) {
                val dummy = TagViewDataDetail(TagDetail(999999,"x",0), false)

                val newTagList = TagViewDataDetail(
                    value = tagViewDataDetail.value,
                    status = !tagViewDataDetail.status
                )

                viewModel.editItemTagList(newTagList,position,dummy)
                Log.d("test_tag_click", "$position $code $itemClick")
            }
        })

    }

    fun addItem(tagList : MutableList<TagViewDataDetail>){
        Log.d("test_addItem",tagList.toString())
        //tagAdapter.submitList(mutableListOf())
        tagAdapter.submitList(tagList)
        tagAdapter.notifyDataSetChanged()
    }

//    private fun tagFlyPosition(valueList: MutableList<Int?>){ ///[0,1,2,3,4,5]
//        val allMapData = viewModel.mapInformationResponse.value?.mapPoints
//        val bookMarkId = viewModel.allMarkerBookmarked.value //Id
//        val currentPosition = viewModel.userLocation.value
//
//        val setOfMarkerPosition = mutableListOf<Point>()
//
//        allMapData?.forEach {
//            if (valueList.contains(100)){
//                var buff = allMapData.filter { allMapData -> bookMarkId!!.any { id -> id.toLong() == allMapData.id} }
//                var buffer = buff.map { Point.fromLngLat(it.longitude,it.latitude) }
//                setOfMarkerPosition.plus(buffer)
//            }
//            if (valueList.contains(tagTypeListUtil.typeToTagCode(it.type))){
//                setOfMarkerPosition.add(Point.fromLngLat(it.longitude,it.latitude))
//            }
//        }
//
//        Log.d("test_tagfly",setOfMarkerPosition.toString())
//        viewModel.updatePositionFlyer(Point.fromLngLat(0.00,0.00))
//
//    }

//    private fun tagFlyPosition(valueList: MutableList<Int?>) {
//        val allMapData = viewModel.mapInformationResponse.value?.mapPoints
//        val bookMarkId = viewModel.allMarkerBookmarked.value
//        val currentPosition = viewModel.userLocation.value
//        //val currentLong = currentPosition?.longitude()
//        //val currentLat = currentPosition?.latitude()
//
//        val setOfMarkerPosition = allMapData?.filter {
//            (valueList.contains(100) && bookMarkId!!.any { id -> id.toLong() == it.id }) ||
//                    valueList.contains(tagTypeListUtil.typeToTagCode(it.type))
//        }?.map { Point.fromLngLat(it.longitude, it.latitude) }
//
//        Log.d("test_tagfly", setOfMarkerPosition.toString())
//
//        viewModel.updatePositionFlyer(Point.fromLngLat(0.00, 0.00))
//
//        val centerOfAllPoint = ????
//
//    }

    private fun tagFlyPosition(valueList: MutableList<Int?>) {
        val allMapData = viewModel.mapInformationResponse.value?.mapPoints
        val bookMarkId = viewModel.allMarkerBookmarked.value
        val currentPosition = viewModel.userLocation.value ?:Point.fromLngLat(100.77820011,13.73005564)

        val filteredMapData = allMapData?.filter {
            (valueList.contains(100) && bookMarkId!!.any { id -> id.toLong() == it.id }) ||
                    valueList.contains(tagTypeListUtil.typeToTagCode(it.type))
        }

        //calculate center of all point
        val filteredPoints = filteredMapData?.filter {
            isWithinKm(
                it.latitude,
                it.longitude,
                currentPosition?.latitude() ?: 0.0,
                currentPosition?.longitude() ?: 0.0
            )
        }?.map { Point.fromLngLat(it.longitude, it.latitude) }

        var centerOfAllPoint = if (filteredPoints.isNullOrEmpty()) {
            Point.fromLngLat(0.0, 0.0)
        } else {
            val sumLat = filteredPoints.sumOf { it.latitude() }
            val sumLng = filteredPoints.sumOf { it.longitude() }
            val avgLat = sumLat / filteredPoints.size
            val avgLng = sumLng / filteredPoints.size
            Point.fromLngLat(avgLng, avgLat)
        }
        centerOfAllPoint = if(valueList.isEmpty() || filteredPoints!!.isEmpty()) currentPosition else centerOfAllPoint

        Log.d("test_tagfly", filteredPoints.toString())

        val setOfMarkerPosition = filteredMapData?.map { Point.fromLngLat(it.longitude, it.latitude) }
        val maxDistance = calculateMaxDistance(setOfMarkerPosition)
        var zoomLevel = calculateZoomLevel(maxDistance,viewModel.screenSize.value?.width!!.toInt())
        zoomLevel = if(valueList.isNotEmpty()) zoomLevel else 18.0

        Log.d("test_maxDistance",maxDistance.toString())
        Log.d("test_zoomLV",zoomLevel.toString())

        viewModel.flyToTagLocation(centerOfAllPoint,zoomLevel)
    }

    private fun isWithinKm(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Boolean {
        val earthRadius = 6371 // Radius of the earth in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distance = earthRadius * c

        return distance <= 30
    }

    private fun calculateMaxDistance(setOfMarkerPosition: List<Point?>?) : Double{
        var maxDistance = 0.0
        if (setOfMarkerPosition != null){
            for (i in 0 until setOfMarkerPosition?.size!! - 1) {
                for (j in i + 1 until setOfMarkerPosition.size) {
                    val distance = distanceBetween(setOfMarkerPosition[i]!!, setOfMarkerPosition[j]!!)
                    if (distance > maxDistance) {
                        maxDistance = distance
                    }
                }
            }
        }
        return maxDistance
    }

    private fun distanceBetween(p1: Point, p2: Point): Double {
        val earthRadius = 6371.0 //in kilometers
        val lat1 = p1.latitude() * Math.PI / 180.0
        val lat2 = p2.latitude() * Math.PI / 180.0
        val lon1 = p1.longitude() * Math.PI / 180.0
        val lon2 = p2.longitude() * Math.PI / 180.0
        val deltaLat = lat2 - lat1
        val deltaLon = lon2 - lon1
        val a = sin(deltaLat / 2) * sin(deltaLat / 2) +
                cos(lat1) * cos(lat2) *
                sin(deltaLon / 2) * sin(deltaLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

    private fun calculateZoomLevel(distance: Double, screenWidth: Int): Double {
        val equatorLength = 40075004.0 // in meters
        val widthInMeters = distance * screenWidth
        var zoomLevel = ln(equatorLength / widthInMeters) / ln(2.35)

        if (zoomLevel.isInfinite()){
            zoomLevel = 18.0
        }
        return zoomLevel
    }

    fun setTagDisplay(valueList: MutableList<TagViewDataDetail>){
        val newList = mutableListOf<Int?>()
        valueList.forEach {
            if (it.status == true){
                newList.add(it.value.code)
            }
        }
        showTagOnMap(newList)
        tagFlyPosition(newList)
    }

    private fun showTagOnMap(valueList: MutableList<Int?>){
        mapView?.getMapboxMap()?.getStyle { it ->
            it.removeStyleLayer(mapExpressionUtils.getLocationLayersID())

            it.addLayer(
                symbolLayer(mapExpressionUtils.getLocationLayersID()
                    , mapExpressionUtils.getLocationSourceID()) {
                    sourceLayer(mapExpressionUtils.getLocationSourceID())

                    if (valueList.isNotEmpty()){

                        val ex = Expression.switchCase {
                                    if(valueList.contains(100)) { //100 = BOOKAMRK TAG CODE
                                        viewModel.allMarkerBookmarked.value?.forEach { markerId ->
                                            eq {
                                                get("id")
                                                literal(markerId.toLong())
                                            }
                                            literal(true)
                                        }
                                    }

                                    valueList.forEach { i ->
                                        eq {
                                            get("tag")
                                            literal(i!!.toLong())
                                        }
                                        literal(true)
                                    }

                                    //dummy
                                    eq {
                                        get("tag")
                                        literal(999999999)
                                    }
                                    literal(true)

                                    literal(false)
                                }
                        //Log.d("test_expression",ex.toString())
                        filter(ex)
//                        filter(
//                            Expression.match {
//                                get("tag")
//                                valueList.forEach { i ->
//                                    stop {
//                                        literal(i!!.toLong())
//                                        literal(true)
//                                    }
//                                }
//
//                                literal(false)
//                            }
//                        )
                    }

                    iconImage(mapExpressionUtils.getImageExpression())

                    iconAllowOverlap(true)
                    iconAnchor(IconAnchor.BOTTOM)
                }
            )
        }
    }

//    fun test() {
//        val mapboxMap = mapView?.getMapboxMap()
//        val center = mapboxMap?.cameraState?.center
//        val screenPoint = mapboxMap?.pixelForCoordinate(center!!)!!
//        val queryOptions = RenderedQueryOptions(listOf(mapExpressionUtils.getLocationLayersID()) , null)
//        val sourceOpitons = SourceQueryOptions(listOf(ViewMap.SOURCE_ID) , Value.nullValue())
//
//        val s = mapboxMap.getStyle()?.getSource(ViewMap.SOURCE_ID)
//
////        //mapboxMap?.cameraState?.scre
////        Log.d("test_map","it's work ${listOf(mapExpressionUtils.getLocationLayersID())} ")
////        mapboxMap?.queryRenderedFeatures(RenderedQueryGeometry(screenPoint!!),queryOptions)   { expect ->
////            val queriedFeature: List<QueriedFeature> = expect.value ?: emptyList()
////            if (queriedFeature.isNotEmpty()) {
////                //isFound = true
////                val selectedFeature = queriedFeature[0].feature
////                Log.d("test_map",selectedFeature.toString())
////            }
////        }
////
////
//        mapboxMap?.querySourceFeatures(ViewMap.SOURCE_ID,sourceOpitons) { expect ->
//            val queriedFeature: List<QueriedFeature> = expect.value ?: emptyList()
//            if (queriedFeature.isNotEmpty()) {
//                //isFound = true
//                val selectedFeature = queriedFeature.forEach { it ->
//                    Log.d("test_source",it.feature.toString())
//                }
//            }
//        }
//
//    }


    fun getItemList(code : Int) : MutableList<Int?>{
        return if (itemClick.contains(code)){
            itemClick.remove(code)
            itemClick
        }else{
            itemClick.add(code)
            itemClick.sortedBy { it }
            itemClick
        }
    }

    fun resetTagClicked(){
        //tagAdapter.submitList(mutableListOf())
        //addItem()
        tagAdapter.submitList(
            tagTypeListUtil.getAvailableMutableListOfTagTypeDetails().mapIndexed { index, tagDetail ->
                TagViewDataDetail(tagDetail, false)
            }.toMutableList()
        )
        tagAdapter.notifyDataSetChanged()
    }

    private val mapView
        get() = weakMapView?.get()

//    private val tagRecyclerView
//        get() = weakTagRecyclerView?.get()

}