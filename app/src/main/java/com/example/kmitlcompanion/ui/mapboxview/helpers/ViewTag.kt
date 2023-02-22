package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.content.Context
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
import com.example.kmitlcompanion.ui.mapboxview.utils.TagZoomCalculateUtils
import com.google.android.material.card.MaterialCardView
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.fillLayer
import com.mapbox.maps.extension.style.layers.generated.symbolLayer
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import java.lang.ref.WeakReference
import javax.inject.Inject


class ViewTag @Inject constructor(
    private val tagAdapter: TagAdapter,
    private val tagTypeListUtil: TagTypeListUtil,
    private val mapExpressionUtils: MapExpressionUtils,
    private val tagZoomCalculateUtils: TagZoomCalculateUtils,
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

    private fun test(valueList: MutableList<Int?>){
        val allEventData = viewModel.mapEventResponse.value?.eventPoints
        val bookEventMarkId = viewModel.allEventBookmarkedIdList.value

        var filteredMapEventData = allEventData?.filter {
            (valueList.contains(100) && bookEventMarkId!!.any { id -> id.toLong() == it.id })||
                    valueList.contains(tagTypeListUtil.getEventTagCode())
        }

        Log.d("test_event_cal",filteredMapEventData.toString())

    }

    private fun tagFlyPosition(valueList: MutableList<Int?>) {
        val allMapData = viewModel.mapInformationResponse.value?.mapPoints
        val allEventData = viewModel.mapEventResponse.value?.eventPoints
        val bookMarkId = viewModel.allMarkerBookmarked.value
        val bookEventMarkId = viewModel.allEventBookmarkedIdList.value
        val currentPosition = viewModel.userLocation.value ?:Point.fromLngLat(100.77820011,13.73005564)

        var filteredMapData = allMapData?.filter {
            (valueList.contains(100) && bookMarkId!!.any { id -> id.toLong() == it.id }) ||
                    valueList.contains(tagTypeListUtil.typeToTagCode(it.type))
        }

        var filteredMapEventData = allEventData?.filter {
            (valueList.contains(100) && bookEventMarkId!!.any { id -> id.toLong() == it.id })||
                    valueList.contains(tagTypeListUtil.getEventTagCode())
        }
        var filteredPoints : List<Point>?
        var eventFilteredPoint : List<Point>?

        //ถ้าไม่ใช่ bookmark ให้ filter ระยะไม่เกิน x กิโลเมตร
        if (!valueList.contains(100)){
            filteredPoints = filteredMapData?.filter {
                tagZoomCalculateUtils.isWithinKm(
                    it.latitude,
                    it.longitude,
                    currentPosition?.latitude() ?: 0.0,
                    currentPosition?.longitude() ?: 0.0
                )
            }?.map { Point.fromLngLat(it.longitude, it.latitude) }
            var filteredAreaPoint = tagZoomCalculateUtils.filterEventAreasWithin(currentPosition,filteredMapEventData)
            eventFilteredPoint = tagZoomCalculateUtils.extractPoints(filteredAreaPoint)
        }else{
            filteredPoints = filteredMapData?.map { Point.fromLngLat(it.longitude,it.latitude) }
            eventFilteredPoint = filteredMapEventData?.let { tagZoomCalculateUtils.extractPoints(it) }
        }

        //calculate center of all marker point
        var filteredAllPoints = (filteredPoints.orEmpty() + eventFilteredPoint.orEmpty()) as MutableList
        filteredAllPoints.add(currentPosition)

        Log.d("test_event",filteredPoints?.size.toString())
        Log.d("test_event",eventFilteredPoint?.size.toString())
        Log.d("test_event",filteredAllPoints?.size.toString())

        var centerOfAllPoint = if (filteredAllPoints.isNullOrEmpty()) {
            Point.fromLngLat(0.0, 0.0)
        } else {
            val sumLat = filteredAllPoints.sumOf { it.latitude() }
            val sumLng = filteredAllPoints.sumOf { it.longitude() }
            val avgLat = sumLat / filteredAllPoints.size
            val avgLng = sumLng / filteredAllPoints.size
            Point.fromLngLat(avgLng, avgLat)
        }
        centerOfAllPoint = if(valueList.isEmpty() || filteredAllPoints!!.isEmpty()) currentPosition else centerOfAllPoint

        Log.d("test_tagfly", filteredAllPoints.toString())

        val setOfMarkerPosition = filteredAllPoints//filteredMapData?.map { Point.fromLngLat(it.longitude, it.latitude) }
        val maxDistance = tagZoomCalculateUtils.calculateMaxDistance(setOfMarkerPosition)
        var zoomLevel = tagZoomCalculateUtils.calculateZoomLevel(maxDistance,viewModel.screenSize.value?.width!!.toInt())
        zoomLevel = if(valueList.isNotEmpty()) zoomLevel else 18.0

        Log.d("test_eventmaxDistance",maxDistance.toString())
        Log.d("test_eventzoomLV",zoomLevel.toString())

        viewModel.flyToTagLocation(centerOfAllPoint,zoomLevel)
    }

    fun setTagDisplay(valueList: MutableList<TagViewDataDetail>){
        val newList = mutableListOf<Int?>()
        valueList.forEach {
            if (it.status == true){
                newList.add(it.value.code)
            }
        }
        Log.d("test_event",newList.toString())

        showTagEventOnMap(newList)
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

    private fun showTagEventOnMap(valueList: MutableList<Int?>){
        var allEventBookmarkIdList = viewModel.allEventBookmarkedIdList.value

        mapView?.getMapboxMap()?.getStyle { it ->
            it.removeStyleLayer(mapExpressionUtils.getLocationAreaLayersID())

            it.addLayer(
                fillLayer(mapExpressionUtils.getLocationAreaLayersID()
                    , mapExpressionUtils.getLocationAreaID()) {

                    if (valueList.isNotEmpty()) {
                        if (!valueList.contains(tagTypeListUtil.getEventTagCode())) {
                            val ex = Expression.switchCase {
                                if (valueList.contains(100)) { //100 = BOOKAMRK TAG CODE
                                    allEventBookmarkIdList?.forEach { eventId ->
                                        eq {
                                            get("id")
                                            literal(eventId.toLong())
                                        }
                                        literal(true)
                                    }
                                }

                                eq {
                                    get("id")
                                    literal(999999999)
                                }
                                literal(true)

                                literal(false)
                            }
                            filter(ex)
                        }
                    }

                    fillColor("#fff000")
                    fillOpacity(0.3)

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