package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.domain.ObservableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class getUserRoom @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : ObservableUseCase<List<UserData>, Nothing?>(postExecutionThread){

    override fun buildUseCaseObservable(params: Nothing?): Observable<List<UserData>> {
        return domainRepository.getUser()
    }
}