package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.domain.ObservableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class postLogin @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
): ObservableUseCase<Int,String>(postExecutionThread){

    public override fun buildUseCaseObservable(params: String?): Observable<Int> {
        return domainRepository.postLogin(
            params?:"",
        )
    }

//    public override fun buildUseCaseCompletable(params: String?): Observable<LoginData> {
//        return domainRepository.postLogin(
//            params?:"",
//        )
//    }

}