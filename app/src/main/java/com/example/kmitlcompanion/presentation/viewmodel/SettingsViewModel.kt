package com.example.kmitlcompanion.presentation.viewmodel

import android.app.Activity
import android.util.Log
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.domain.usecases.UpdateUser
import com.example.kmitlcompanion.presentation.BaseViewModel
import com.example.kmitlcompanion.presentation.eventobserver.Event
import com.example.kmitlcompanion.presentation.utils.SingleLiveData
import com.example.kmitlcompanion.ui.settings.SettingsFragmentDirections
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import javax.inject.Inject
import com.google.android.gms.tasks.OnCompleteListener
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import kotlin.math.E

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val updateUser: UpdateUser
    ) : BaseViewModel() {


    //update user data in room
    private val _updateUserRoom = SingleLiveData<UserData>()
    val updateUserRoom: SingleLiveData<UserData> = _updateUserRoom

    private val _signOut = SingleLiveData<Event<Boolean>>()
    val signOut: SingleLiveData<Event<Boolean>> = _signOut


    //room update user
    fun updateUser(userData: UserData) {
        updateUser.execute(object : DisposableCompletableObserver(){
            override fun onComplete() {
                Log.d("UpdateUser","Update UserData Complete!!!")
                gotoLogin()
            }

            override fun onError(e: Throwable) {
                Log.d("UpdateUser",e.toString())
            }
        },userData)
    }

    fun signOut(){
        _signOut.value = Event(true)
    }

    fun gotoLogin(){
        Log.d("Logout","Logout")
        navigate(SettingsFragmentDirections.actionSettingsFragmentToLoginFragment())
    }

    fun goBackClicked() {
        navigateBack()
    }

}