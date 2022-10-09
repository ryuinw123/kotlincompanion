package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class CreateLocationQuery @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
): CompletableUseCase<Pair<Double,Double>?>(postExecutionThread){
    public override fun buildUseCaseCompletable(params: Pair<Double, Double>?): Completable {
        return domainRepository.createLocationQuery(
            params?.first?:0.0,
            params?.second?:0.0
        )
    }

}