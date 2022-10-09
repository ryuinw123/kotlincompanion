package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class PostToken @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
): CompletableUseCase<String?>(postExecutionThread){

    public override fun buildUseCaseCompletable(params: String?): Completable {
        return domainRepository.postToken(
            params?:"",
        )
    }

}