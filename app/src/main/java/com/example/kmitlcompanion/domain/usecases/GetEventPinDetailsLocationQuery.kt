package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.domain.ObservableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.PinDetail
import com.example.kmitlcompanion.domain.model.PinEventDetail
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetEventPinDetailsLocationQuery @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : ObservableUseCase<PinEventDetail,String>(postExecutionThread) {

    override fun buildUseCaseObservable(params: String?): Observable<PinEventDetail> {
        return domainRepository.getEventDetailsLocationQuery(
            id = params ?:""
        )
    }

}