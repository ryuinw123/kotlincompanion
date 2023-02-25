package com.example.kmitlcompanion.domain.usecases

import android.util.Log
import com.example.kmitlcompanion.data.model.LocationData
import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.EditLocation
import com.example.kmitlcompanion.domain.model.Location
import com.example.kmitlcompanion.domain.repository.DomainRepository
import com.mapbox.geojson.Point
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class EditLocationQuery @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : CompletableUseCase<EditLocation>(postExecutionThread) {

    override fun buildUseCaseCompletable(params: EditLocation?): Completable {
        return domainRepository.editLocationQuery(
            id = params?.id ?:"",
            name = params?.name ?:"",
            type = params?.type ?:"",
            description = params?.description ?:"",
            image = (params?.image ?: mutableListOf()) as MutableList<Pair<Int, Any>>,
        )
    }
}