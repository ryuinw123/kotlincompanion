package com.example.kmitlcompanion.ui.auth.helpers

import androidx.lifecycle.DefaultLifecycleObserver
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.presentation.viewmodel.LoginViewModel
import javax.inject.Inject

import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
class AuthHelper @Inject constructor() : DefaultLifecycleObserver {

    private lateinit var viewModel: LoginViewModel

    fun setup(viewModel: LoginViewModel){
        this.viewModel = viewModel
    }

    fun updateUserRoom(userData: UserData){
        this.viewModel.updateUser(userData)
    }

    fun postLogin(authCode: String){
        viewModel.postLogin(authCode)
    }

    fun signInHome(){
        viewModel.nextHomePage()
    }

    fun signInIdentity(){
        viewModel.nextIdentityPage()
    }

    fun signOut_failed(){
        viewModel.signOut()
    }




}