package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.EditEventLocation
import com.example.kmitlcompanion.domain.model.Event
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class EditEventQuery @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : CompletableUseCase<EditEventLocation>(postExecutionThread) {

    override fun buildUseCaseCompletable(params: EditEventLocation?): Completable {
        return domainRepository.editEventLocationQuery(
            eventId = params?.eventId ?:"",
            name = params?.name ?:"",
            description = params?.description ?:"",
            image = (params?.image ?: mutableListOf()) as MutableList<Pair<Int, Any>>,
        )
    }
}