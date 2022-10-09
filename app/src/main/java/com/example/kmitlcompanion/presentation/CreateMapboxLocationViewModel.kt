package com.example.kmitlcompanion.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kmitlcompanion.domain.usecases.CreateLocationQuery
import com.example.kmitlcompanion.presentation.eventobserver.Event
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import javax.inject.Inject

@HiltViewModel
class CreateMapboxLocationViewModel @Inject constructor(
    private val createLocationQuery: CreateLocationQuery
) : ViewModel() {

    private val _currentMapLocation = MutableLiveData<String?>()
    val currentMapLocation : LiveData<String?> = _currentMapLocation

    private val _createLocation = MutableLiveData<Event<Boolean>>()
    val createLocation: LiveData<Event<Boolean>> = _createLocation


    fun createLocation() {
        _createLocation.value = Event(true)
    }

    fun createLocation(point: Point?) {
        if (point != null) {
            createLocationQuery.execute(object : DisposableCompletableObserver() {
                override fun onComplete() {
                    println("CompleteCreate")
                }

                override fun onError(e: Throwable) {
                    TODO("Not yet implemented")
                }
            }, Pair(point.latitude(),point.longitude()))
        }

    }
    fun updateCurrentMapLocation(point : Point?) {
        val lat = point?.latitude().toString()
        val long = point?.longitude().toString()
        _currentMapLocation.value = "Lat : $lat, Long: $long"
        println(_currentMapLocation.value)
    }
}