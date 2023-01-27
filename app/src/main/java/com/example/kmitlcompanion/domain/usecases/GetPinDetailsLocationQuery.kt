package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.domain.ObservableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.LikeDetail
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetPinDetailsLocationQuery @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : ObservableUseCase<LikeDetail,String>(postExecutionThread) {
    override fun buildUseCaseObservable(params: String?): Observable<LikeDetail> {
        return domainRepository.getPinDetailsLocationQuery(
            id = params!!
        )
    }

}