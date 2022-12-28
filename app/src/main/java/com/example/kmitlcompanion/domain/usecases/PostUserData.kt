package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class PostUserData @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
): CompletableUseCase<ArrayList<Any>?>(postExecutionThread) {

    override fun buildUseCaseCompletable(params: ArrayList<Any>?): Completable {
        return domainRepository.postUserData(
            params?.get(0)?:"",
            params?.get(1)?:"",
            params?.get(2)?:"",
            params?.get(3)?:"",
            params?.get(4)?:0,
            params?.get(5)?:""
        )
    }

}