package com.example.kmitlcompanion.domain.usecases

import android.util.Log
import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.domain.model.UserEditData
import com.example.kmitlcompanion.domain.model.UserSettingsData
import com.example.kmitlcompanion.domain.repository.DomainRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class SettingsEditUpdateUserData @Inject constructor(
    postExecutionThread: PostExecutionThread,
    private val domainRepository: DomainRepository
) : CompletableUseCase<UserEditData>(postExecutionThread) {

    override fun buildUseCaseCompletable(params: UserEditData?): Completable {
        return domainRepository.settingsEditUpdateUserData(
            userEditData = params!!
        )
    }
}