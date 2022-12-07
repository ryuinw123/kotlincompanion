package com.example.kmitlcompanion.ui.auth.helpers

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.presentation.viewmodel.LoginViewModel
import com.example.kmitlcompanion.domain.model.DomainUserData
import javax.inject.Inject

import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
class AuthHelper @Inject constructor() {

    private lateinit var viewModel: LoginViewModel

    fun setup(viewModel: LoginViewModel){
        this.viewModel = viewModel
    }

    fun updateUserRoom(domainUserData: DomainUserData){
        Log.d("Helper UpdateuserRoom ",domainUserData.toString())
        this.viewModel.updateUser(domainUserData)
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