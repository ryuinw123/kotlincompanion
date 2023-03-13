package com.example.kmitlcompanion.domain.usecases

import android.util.Log
import com.example.kmitlcompanion.domain.ObservableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Observable
import java.sql.Timestamp
import javax.inject.Inject

class GetLastestNotificationTime @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : ObservableUseCase<Timestamp, Int?>(postExecutionThread){

    override fun buildUseCaseObservable(params: Int?): Observable<Timestamp> {
        return domainRepository.getLastestNotificationTime(params!!)
    }
}