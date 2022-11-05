package com.example.kmitlcompanion.ui.settings.helper

import androidx.lifecycle.DefaultLifecycleObserver
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.presentation.SettingsViewModel
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class SettingHelper @Inject constructor() : DefaultLifecycleObserver {
    private lateinit var viewModel: SettingsViewModel

    fun setup(viewModel: SettingsViewModel){
        this.viewModel = viewModel
    }

    fun updateUserRoom(userData: UserData){
        this.viewModel.updateUser(userData)
    }
}