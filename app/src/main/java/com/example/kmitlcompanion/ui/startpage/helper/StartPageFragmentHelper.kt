package com.example.kmitlcompanion.ui.startpage.helper

import android.app.Activity
import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.presentation.viewmodel.StartPageViewModel
import com.example.kmitlcompanion.ui.mapboxview.utils.LocationPermissionHelper
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class StartPageFragmentHelper @Inject constructor(
    private val locationPermissionHelper: LocationPermissionHelper,
) : DefaultLifecycleObserver {
    private lateinit var viewModel: StartPageViewModel

    fun setup(viewModel: StartPageViewModel){
        this.viewModel = viewModel
    }

    fun checkPermission(context: Context,activity: Activity,callBack: () -> Unit){
        locationPermissionHelper.checkPermissions(context,activity) {
            callBack()
        }
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