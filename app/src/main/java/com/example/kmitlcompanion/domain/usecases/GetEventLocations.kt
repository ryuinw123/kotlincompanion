package com.example.kmitlcompanion.domain.usecases

import android.util.Log
import com.example.kmitlcompanion.domain.ObservableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.EventArea
import com.example.kmitlcompanion.domain.model.EventInformation
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetEventLocations @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : ObservableUseCase<EventInformation,Nothing?>(postExecutionThread){

    override fun buildUseCaseObservable(params: Nothing?): Observable<EventInformation> {
        return domainRepository.getEventLocations()
    }
}