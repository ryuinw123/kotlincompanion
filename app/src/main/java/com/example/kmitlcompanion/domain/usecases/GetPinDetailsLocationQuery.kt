package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.domain.ObservableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.PinDetail
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetPinDetailsLocationQuery @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : ObservableUseCase<PinDetail,String>(postExecutionThread) {
    override fun buildUseCaseObservable(params: String?): Observable<PinDetail> {
        return domainRepository.getPinDetailsLocationQuery(
            id = params!!
        )
    }

}