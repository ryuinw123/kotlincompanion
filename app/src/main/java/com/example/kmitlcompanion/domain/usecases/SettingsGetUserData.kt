package com.example.kmitlcompanion.domain.usecases

import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.ObservableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.UserSettingsData
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class SettingsGetUserData @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : ObservableUseCase<UserSettingsData,Nothing?>(postExecutionThread) {

    override fun buildUseCaseObservable(params: Nothing?): Observable<UserSettingsData> {
        return domainRepository.settingsGetUserData()
    }
}