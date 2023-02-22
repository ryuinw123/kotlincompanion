package com.example.kmitlcompanion.dbomain.usecases

import com.example.kmitlcompanion.domain.ObservableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.SearchDetail
import com.example.kmitlcompanion.domain.repository.DomainRepository
import com.mapbox.geojson.Point
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class getSearchDetailsQuery @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
): ObservableUseCase<List<SearchDetail>,Triple<String?,MutableList<Int?>, Point?>>(postExecutionThread) {

    override fun buildUseCaseObservable(params: Triple<String?, MutableList<Int?>, Point?>?): Observable<List<SearchDetail>> {
        return domainRepository.getSearchDetailsQuery(
            text = params?.first ?:"",
            typeList = params?.second ?: mutableListOf(),
            latitude = params?.third?.latitude() ?:0.0,
            longitude = params?.third?.longitude() ?:0.0,
        )
    }

//    override fun buildUseCaseObservable(params: Pair<String?, MutableList<Int?>>?): Observable<List<SearchDetail>> {
//        return domainRepository.getSearchDetailsQuery(
//            text = params?.first ?:"",
//            typeList = params?.second ?: mutableListOf(),
//        )
//    }
}