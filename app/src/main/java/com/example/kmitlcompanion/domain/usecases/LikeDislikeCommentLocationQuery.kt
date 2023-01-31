package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class LikeDislikeCommentLocationQuery @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : CompletableUseCase<Triple<String,Boolean,Boolean>>(postExecutionThread) {

    override fun buildUseCaseCompletable(params: Triple<String, Boolean, Boolean>?): Completable {
        return domainRepository.likeDislikeCommentLocationQuery(
            commentId = params?.first ?:"",
            isLikedComment = if (params?.second!!) 1 else 0 ,
            isDisLikedComment = if (params?.third!!) 1 else 0,
        )
    }
}