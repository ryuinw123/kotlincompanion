package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.DomainUserData
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class UpdateUser @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : CompletableUseCase<DomainUserData>(postExecutionThread) {
    override fun buildUseCaseCompletable(params: DomainUserData?): Completable {
        return domainRepository.updateUser(
            params?.email?:"",
            params?.token?:""
        )
    }


}