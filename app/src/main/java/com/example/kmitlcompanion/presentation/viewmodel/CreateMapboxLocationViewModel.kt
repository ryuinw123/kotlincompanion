package com.example.kmitlcompanion.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kmitlcompanion.dbomain.usecases.GetLocationQuery
import com.example.kmitlcompanion.domain.model.LocationDetail
import com.example.kmitlcompanion.domain.usecases.CreateLocationQuery
import com.example.kmitlcompanion.presentation.BaseViewModel
import com.example.kmitlcompanion.presentation.eventobserver.Event
import com.example.kmitlcompanion.ui.createmapboxlocation.CreateMapboxLocationFragmentDirections
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.observers.DisposableObserver
import javax.inject.Inject

@HiltViewModel
class CreateMapboxLocationViewModel @Inject constructor(
    private val getLocationQuery: GetLocationQuery
) : BaseViewModel() {

    private val _currentMapLocation = MutableLiveData<Point?>()
    val currentMapLocation : LiveData<Point?> = _currentMapLocation


    private val _currentLocation = MutableLiveData<LocationDetail>()
    val currentLocation: LiveData<LocationDetail> = _currentLocation




    /*fun createLocation(point: Point?) {
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

    }*/
    fun updateCurrentMapLocation(point : Point?) {
        val lat = point?.latitude().toString()
        val long = point?.longitude().toString()
        _currentMapLocation.value = point //"Lat : $lat, Long: $long"
        println(_currentMapLocation.value)
    }

    fun getLocationQuery(point: Point?){
        if (point != null) {
            getLocationQuery.execute(object : DisposableObserver<LocationDetail>() {
                override fun onNext(t: LocationDetail) {
                    _currentLocation.value  = t
                }

                override fun onError(e: Throwable) {
                    TODO("Not yet implemented")
                }

                override fun onComplete() {
                }
            }, Pair(point.latitude(),point.longitude()))
        }
    }

    fun goToCreateLocation(){
        navigate(
            CreateMapboxLocationFragmentDirections.actionCreateMapboxLocationFragment2ToCreateLocation(
                currentLocation = currentLocation.value!!,
            )
        )
    }

    fun goBackClicked() {
        navigateBack()
    }

}