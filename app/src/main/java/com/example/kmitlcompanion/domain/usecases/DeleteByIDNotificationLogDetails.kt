package com.example.kmitlcompanion.domain.usecases

import android.util.Log
import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.NotiLogDetails
import com.example.kmitlcompanion.domain.model.UserEditData
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class DeleteByIDNotificationLogDetails @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : CompletableUseCase<Long?>(postExecutionThread) {

    override fun buildUseCaseCompletable(params: Long?): Completable {
        return domainRepository.deleteByIDNotificationLogDetails(id = params ?:0)
    }
}