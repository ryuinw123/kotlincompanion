package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class UpdateUser @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : CompletableUseCase<UserData>(postExecutionThread) {
    override fun buildUseCaseCompletable(params: UserData?): Completable {
        return domainRepository.updateUser(
            params?.email?:"",
            params?.token?:""
        )
    }


}