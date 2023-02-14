package com.example.kmitlcompanion.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kmitlcompanion.domain.model.EventDetail
import com.example.kmitlcompanion.domain.model.LocationDetail
import com.example.kmitlcompanion.presentation.BaseViewModel
import com.example.kmitlcompanion.ui.createcircleevent.CreateCircleEventFragmentDirections
import com.example.kmitlcompanion.ui.createmapboxlocation.CreateMapboxLocationFragmentDirections
import com.mapbox.geojson.Point
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.function.DoubleBinaryOperator
import javax.inject.Inject

@HiltViewModel
class CreateCircleEventViewModel @Inject constructor(

) : BaseViewModel() {

    private val _currentPolygon = MutableLiveData<List<Point>>()
    val currentPolygon : LiveData<List<Point>> = _currentPolygon

    private val _currentRadius = MutableLiveData<Double>()
    val currentRadius : LiveData<Double> = _currentRadius

    private val _currentPin = MutableLiveData<PointAnnotation?>()
    val currentPin : LiveData<PointAnnotation?> = _currentPin

    private val _positionFlyer = MutableLiveData<Point>()
    val positionFlyer : LiveData<Point> = _positionFlyer


    fun goToCreateLocation(){
        navigate(
            CreateCircleEventFragmentDirections.actionCreateCircleEventFragmentToCreateEventFragment(
                event = EventDetail(
                    point = currentPolygon.value!!
                ),
            )
        )
    }

    fun updateCurrentPolygon(polygon : List<Point>) {
        _currentPolygon.value = polygon
    }

    fun updatePositionFlyer(point: Point) {
        _positionFlyer.value = point
    }


    fun updateCurrentPin(pointAnnotation: PointAnnotation) {
        _currentPin.value = pointAnnotation
    }
    fun updateRadius(radius : Double) {
        _currentRadius.value = radius
    }
}