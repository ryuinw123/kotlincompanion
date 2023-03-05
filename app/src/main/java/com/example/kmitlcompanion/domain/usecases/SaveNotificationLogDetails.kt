package com.example.kmitlcompanion.domain.usecases

import android.util.Log
import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.NotiLogDetails
import com.example.kmitlcompanion.domain.model.UserEditData
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class SaveNotificationLogDetails @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : CompletableUseCase<NotiLogDetails?>(postExecutionThread) {

    override fun buildUseCaseCompletable(params: NotiLogDetails?): Completable {
        return domainRepository.saveNotificationLogDetails(
            id = params?.id ?:0,
            name = params?.name ?:"",
            startTime = params?.startTime ?:"",
            endTime = params?.endTime ?:"",
        )
    }
}