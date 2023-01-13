package com.example.kmitlcompanion.foreground

import com.example.kmitlcompanion.domain.model.MapInformation
import com.example.kmitlcompanion.domain.usecases.GetMapLocations
import io.reactivex.rxjava3.observers.DisposableObserver
import javax.inject.Inject

class ServiceUseCase @Inject constructor(
    private val getMapLocations: GetMapLocations
) {
    lateinit var mainService: MainService

    fun setup(mainService: MainService) {
        this.mainService = mainService
    }
    fun downloadMap() {
        getMapLocations.execute(object : DisposableObserver<MapInformation>() {
            override fun onComplete() {
                println("Complete")
            }

            override fun onNext(t: MapInformation) {
                mainService.mapInformation = t
            }

            override fun onError(e: Throwable) {
                println("Error")
            }

        })

    }
}