package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.domain.ObservableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.AddCommentDetail
import com.example.kmitlcompanion.domain.model.ReturnAddComment
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class AddCommentMarkerLocationQuery @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : ObservableUseCase<ReturnAddComment,AddCommentDetail>(postExecutionThread) {
    override fun buildUseCaseObservable(params: AddCommentDetail?): Observable<ReturnAddComment> {
        return domainRepository.addCommentMarkerLocationQuery(
            markerId = params?.markerId ?:"",
            message = params?.message ?:""
        )
    }
}