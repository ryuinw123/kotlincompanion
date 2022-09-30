package com.example.kmitlcompanion.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kmitlcompanion.domain.model.MapInformation
import com.example.kmitlcompanion.domain.usecases.GetMapLocations
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.observers.DisposableObserver
import javax.inject.Inject

@HiltViewModel
class MapboxViewModel @Inject constructor(
    private val getMapLocations: GetMapLocations
) : ViewModel() {


    private val _mapInformationResponse = MutableLiveData<MapInformation>()
    val mapInformationResponse: LiveData<MapInformation> = _mapInformationResponse

    fun downloadLocations() {
        getMapLocations.execute(object : DisposableObserver<MapInformation>() {
            override fun onComplete() {
                println("Complete")
            }

            override fun onNext(t: MapInformation) {
                println("FromObserver")
                println(t)
                _mapInformationResponse.value = t
            }

            override fun onError(e: Throwable) {
                println("Error")
            }

        })
    }
}