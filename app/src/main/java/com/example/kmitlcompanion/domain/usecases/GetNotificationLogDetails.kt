package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.domain.ObservableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.NotiLogDetails
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetNotificationLogDetails @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : ObservableUseCase<List<NotiLogDetails>,Nothing?>(postExecutionThread) {

    override fun buildUseCaseObservable(params: Nothing?): Observable<List<NotiLogDetails>> {
        return domainRepository.getNotificationLogDetails()
    }
}