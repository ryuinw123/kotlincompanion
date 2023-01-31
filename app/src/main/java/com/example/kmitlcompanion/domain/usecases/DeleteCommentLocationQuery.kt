package com.example.kmitlcompanion.domain.usecases

import android.util.Log
import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class DeleteCommentLocationQuery @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : CompletableUseCase<String>(postExecutionThread) {
    override fun buildUseCaseCompletable(params: String?,): Completable {
        return domainRepository.deleteCommentLocationQuery(
            commentId = params ?:"",
        )
    }
}