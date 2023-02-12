package com.example.kmitlcompanion.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kmitlcompanion.domain.model.Location
import com.example.kmitlcompanion.domain.model.LocationDetail
import com.example.kmitlcompanion.presentation.BaseViewModel
import com.example.kmitlcompanion.ui.createcircleevent.CreateCircleEventFragmentDirections
import com.example.kmitlcompanion.ui.createpolygonevent.CreatePolygonEventFragmentDirections
import com.mapbox.geojson.Point
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreatePolygonEventViewModel @Inject constructor(

) : BaseViewModel() {


    private val _activePoint = MutableLiveData<MutableList<PointAnnotation>>()
    val activePoint : LiveData<MutableList<PointAnnotation>> = _activePoint

    private val _positionFlyer = MutableLiveData<Point>()
    val positionFlyer : LiveData<Point> = _positionFlyer



    fun goToCreateLocation(){

        val polygon = mutableListOf<List<Double>>()

        for (i in activePoint.value!!) {
            val point = i.point
            val latitude = point.latitude()
            val longitude = point.longitude()

            val lstPoint = listOf(latitude,longitude)
            polygon.add(lstPoint)
        }


        navigate(
            CreatePolygonEventFragmentDirections.actionCreatePolygonEventFragmentToCreateLocation2(
                currentLocation = LocationDetail(
                    place = null,
                    address = null,
                    point = null,
                    polygon = polygon
                ),
            )
        )
    }

    fun updateActivePoint(activePoint: MutableList<PointAnnotation>) {
        _activePoint.value = activePoint
    }


    fun updatePositionFlyer(point: Point) {
        _positionFlyer.value = point
    }

}