package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.data.model.LocationData
import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.Event
import com.example.kmitlcompanion.domain.model.Location
import com.example.kmitlcompanion.domain.repository.DomainRepository
import com.mapbox.geojson.Point
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class CreateEventQuery @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : CompletableUseCase<Event>(postExecutionThread) {

    override fun buildUseCaseCompletable(params: Event?): Completable {
        return domainRepository.createEventQuery(
             Event(
                inputName = params!!.inputName!!,
                 description = params!!.description!!,
                 status = params!!.status!!,
                point = params!!.point,
                file = params!!.file,
                uri = params!!.uri
            )
        )
    }

}