package com.example.kmitlcompanion.ui.startpage.helper

import androidx.lifecycle.DefaultLifecycleObserver
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.presentation.viewmodel.StartPageViewModel
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class StartPageFragmentHelper @Inject constructor() : DefaultLifecycleObserver {
    private lateinit var viewModel: StartPageViewModel

    fun setup(viewModel: StartPageViewModel){
        this.viewModel = viewModel
    }

    fun getUserRoom(){
        viewModel.getUserRoom()
    }

    fun updateUserRoom(userData: UserData){
        this.viewModel.updateUser(userData)
    }

    fun postLogin(authCode: String){
        viewModel.postLogin(authCode)
    }
}