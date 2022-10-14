package com.example.kmitlcompanion.ui.identitylogin.helper

import androidx.lifecycle.DefaultLifecycleObserver
import com.example.kmitlcompanion.presentation.IdentityloginViewModel
import javax.inject.Inject

class IdentityHelper @Inject constructor() : DefaultLifecycleObserver {

    private lateinit var viewModel: IdentityloginViewModel

    fun setup(viewModel: IdentityloginViewModel){
        this.viewModel = viewModel
    }

    fun postUserData(arr : ArrayList<Any>){
        viewModel.postUserData(arr)
    }

    fun nextHomePage(){
        viewModel.nextHomePage()
    }

}