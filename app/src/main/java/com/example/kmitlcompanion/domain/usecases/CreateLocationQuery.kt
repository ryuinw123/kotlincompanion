package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.data.model.LocationData
import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.Location
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class CreateLocationQuery @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : CompletableUseCase<Location>(postExecutionThread) {

    override fun buildUseCaseCompletable(params: Location?): Completable {
        return domainRepository.createLocationQuery(
            location = LocationData(
                inputName = params!!.inputName!!,
                description = params!!.description!!,
                place = params!!.place!!,
                type = params!!.type!!,
                address = params!!.address!!,
                latitude = params!!.point!!.latitude(),
                longitude = params!!.point!!.longitude(),
                file = params!!.file!!,
                uri = params!!.uri!!
            )
        )
    }

}