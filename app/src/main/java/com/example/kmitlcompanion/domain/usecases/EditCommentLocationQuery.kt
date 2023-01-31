package com.example.kmitlcompanion.domain.usecases

import android.util.Log
import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class EditCommentLocationQuery @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : CompletableUseCase<Pair<String, String>>(postExecutionThread) {
    override fun buildUseCaseCompletable(params: Pair<String, String>?): Completable {
        return domainRepository.editCommentLocationQuery(
            commentId = params!!.first ?:"",
            newMessage =  params!!.second ?:"",
        )
    }
}