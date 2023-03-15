package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.NotiLogDetails
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class ReportMarkerLocationQueryDetails @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : CompletableUseCase<Triple<Long?,String?,String?>?>(postExecutionThread) {

    override fun buildUseCaseCompletable(params: Triple<Long?, String?, String?>?): Completable {
        return domainRepository.reportMarkerLocationQueryDetails(
            id = params?.first ?:0,
            reason = params?.second ?:"",
            details = params?.third ?:"",
        )
    }
}