package com.example.kmitlcompanion.ui.settings.helper

import androidx.lifecycle.DefaultLifecycleObserver
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.domain.model.DomainUserData
import com.example.kmitlcompanion.presentation.SettingsViewModel
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class SettingHelper @Inject constructor() {
    private lateinit var viewModel: SettingsViewModel

    fun setup(viewModel: SettingsViewModel){
        this.viewModel = viewModel
    }

    fun updateUserRoom(domainUserData: DomainUserData){
        this.viewModel.updateUser(domainUserData)
    }
}