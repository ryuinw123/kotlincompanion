package com.example.kmitlcompanion.domain.usecases

import android.util.Log
import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.NotiLogDetails
import com.example.kmitlcompanion.domain.model.UserEditData
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class DeleteAllNotificationLogDetails @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : CompletableUseCase<Nothing?>(postExecutionThread) {

    override fun buildUseCaseCompletable(params: Nothing?): Completable {
        return domainRepository.deleteAllNotificationLogDetails()
    }
}