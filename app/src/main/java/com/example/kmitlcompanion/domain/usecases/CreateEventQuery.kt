package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.Event
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class CreateEventQuery @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : CompletableUseCase<Triple<Event,String,String>>(postExecutionThread) {

    override fun buildUseCaseCompletable(params: Triple<Event,String,String>?): Completable {
        return domainRepository.createEventQuery(
             Event(
                 name = params?.first?.name ?:"",
                 description = params?.first?.description ?:"",
                 startTime = params?.second ?:"",
                 endTime = params?.third ?:"",
                 point = params!!.first!!.point,
                 file = params!!.first!!.file,
                 uri = params!!.first!!.uri
             )
        )
    }

}