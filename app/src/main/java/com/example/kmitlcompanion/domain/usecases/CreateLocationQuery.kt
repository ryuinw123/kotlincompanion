package com.example.kmitlcompanion.domain.usecases

import android.util.Log
import com.example.kmitlcompanion.data.model.LocationData
import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.Location
import com.example.kmitlcompanion.domain.repository.DomainRepository
import com.mapbox.geojson.Point
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class CreateLocationQuery @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : CompletableUseCase<Location>(postExecutionThread) {

    override fun buildUseCaseCompletable(params: Location?): Completable {
        val point = Point.fromLngLat(params!!.point!!.longitude(),params!!.point!!.latitude())
        return domainRepository.createLocationQuery(
            location = Location(
                inputName = params!!.inputName!!,
                description = params!!.description!!,
                place = params!!.place!!,
                type = params!!.type!!,
                address = params!!.address!!,
                point = point,
                file = params!!.file,
                uri = params!!.uri
            )
        )
    }

}