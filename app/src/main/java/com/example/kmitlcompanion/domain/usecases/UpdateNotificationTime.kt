package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.EventTime
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class UpdateNotificationTime @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : CompletableUseCase<EventTime>(postExecutionThread) {
    override fun buildUseCaseCompletable(params: EventTime?): Completable {
        return domainRepository.updateNotificationTime(
            params!!
        )
    }
}