package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.domain.ObservableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.MapInformation
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CheckValidCreateMarkerCount @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : ObservableUseCase<Int,Nothing?>(postExecutionThread){

    override fun buildUseCaseObservable(params: Nothing?): Observable<Int> {
        return domainRepository.checkValidCreateMarkerCount()
    }
}