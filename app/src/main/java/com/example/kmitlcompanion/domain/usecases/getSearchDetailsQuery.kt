package com.example.kmitlcompanion.dbomain.usecases

import com.example.kmitlcompanion.domain.ObservableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.SearchDetail
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class getSearchDetailsQuery @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
): ObservableUseCase<List<SearchDetail>,Pair<String?,MutableList<Int?>>>(postExecutionThread) {

    override fun buildUseCaseObservable(params: Pair<String?, MutableList<Int?>>?): Observable<List<SearchDetail>> {
        return domainRepository.getSearchDetailsQuery(
            text = params?.first ?:"",
            typeList = params?.second ?: mutableListOf(),
        )
    }
}