package com.example.kmitlcompanion.ui.auth.helpers

import android.content.Context
import android.view.View
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.presentation.viewmodel.LoginViewModel
import com.example.kmitlcompanion.ui.mapboxview.utils.ToasterUtil
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
class AuthHelper @Inject constructor(
    private val context : Context,
    private val toasterUtil: ToasterUtil,
) {

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


    fun showDialog(text : String,view : View){
        toasterUtil.showToast(text)
    }



}