package com.example.kmitlcompanion.domain.usecases

import android.util.Log
import com.example.kmitlcompanion.data.model.LocationData
import com.example.kmitlcompanion.data.model.LocationPublicData
import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.Location
import com.example.kmitlcompanion.domain.model.LocationPublic
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class CreatePublicLocationQuery @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : CompletableUseCase<LocationPublic>(postExecutionThread) {

    override fun buildUseCaseCompletable(params: LocationPublic?): Completable {
        return domainRepository.createPublicLocationQuery(
            location = LocationPublicData(
                inputName = params!!.inputName!!,
                description = params!!.description!!,
                place = params!!.place!!,
                type = params!!.type!!,
                address = params!!.address!!,
                latitude = params!!.point!!.latitude(),
                longitude = params!!.point!!.longitude(),
                file = params!!.file,
                uri = params!!.uri
            )
        )
    }

}