package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class UpdateBookmakerQuery @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : CompletableUseCase<Pair<String,Boolean>>(postExecutionThread) {

    override fun buildUseCaseCompletable(params: Pair<String, Boolean>?): Completable {
        return domainRepository.updateBookmakerQuery(
            markerId = params?.first ?:"",
            isBookmarked = params?.second!!
        )
    }
}