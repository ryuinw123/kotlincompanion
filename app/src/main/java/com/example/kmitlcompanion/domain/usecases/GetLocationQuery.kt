package com.example.kmitlcompanion.dbomain.usecases

import com.example.kmitlcompanion.domain.ObservableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.LocationDetail
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetLocationQuery @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
): ObservableUseCase<LocationDetail,Pair<Double,Double>>(postExecutionThread) {
    override fun buildUseCaseObservable(params: Pair<Double, Double>?): Observable<LocationDetail> {
        val latitude = String.format("%.8f", params?.first).toDouble()
        val longitude = String.format("%.8f", params?.second).toDouble()



        return domainRepository.getLocationQuery(
            latitude, longitude
        )
    }

}