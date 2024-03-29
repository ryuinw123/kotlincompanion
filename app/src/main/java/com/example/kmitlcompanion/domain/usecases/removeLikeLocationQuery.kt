package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class removeLikeLocationQuery @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : CompletableUseCase<String>(postExecutionThread) {
    override fun buildUseCaseCompletable(params: String?): Completable {
        return domainRepository.removeLikeLocationQuery(
            id = params!!
        )
    }
}