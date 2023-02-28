package com.example.kmitlcompanion.presentation.viewmodel

import android.app.Activity
import android.util.Log
import android.util.LogPrinter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.cache.entities.User
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.domain.model.ReturnAddComment
import com.example.kmitlcompanion.domain.model.UserSettingsData
import com.example.kmitlcompanion.domain.usecases.SettingsGetUserData
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
import io.reactivex.rxjava3.observers.DisposableObserver
import kotlin.math.E

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val updateUser: UpdateUser,
    private val settingsGetUserData: SettingsGetUserData
    ) : BaseViewModel() {


    //update user data in room
    private val _updateUserRoom = SingleLiveData<UserData>()
    val updateUserRoom: SingleLiveData<UserData> = _updateUserRoom

    private val _signOut = SingleLiveData<Event<Boolean>>()
    val signOut: SingleLiveData<Event<Boolean>> = _signOut

    private val _settingsUserData = MutableLiveData<UserSettingsData>()
    val settingsUserData : LiveData<UserSettingsData> = _settingsUserData

    private val navOptions = NavOptions.Builder()
        .setLaunchSingleTop(true)
        .setEnterAnim(R.anim.slide_left) // set enter animation
        .setExitAnim(R.anim.slide_right) // set exit animation
        .setPopEnterAnim(R.anim.slide_left) // set pop enter animation
        .setPopExitAnim(R.anim.slide_right) // set pop exit animation
        .build()

    fun downloadSettings(){
        settingsGetUserData.execute(object : DisposableObserver<UserSettingsData>(){
            override fun onNext(t: UserSettingsData) {
                _settingsUserData.value = t
            }

            override fun onComplete() {
                Log.d("settingsGetUserData","complete")
            }

            override fun onError(e: Throwable) {
                Log.d("settingsGetUserData",e.toString())
            }
        })
    }

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

    fun nameEditClick(){
        navigate(SettingsFragmentDirections.actionSettingsFragmentToSettingsEditFragment(
            username = _settingsUserData.value?.username,
            email = _settingsUserData.value?.email ?:"",
            faculty = _settingsUserData.value?.faculty ?:"",
            department = _settingsUserData.value?.department ?:"",
            year = _settingsUserData.value?.year ?:"",
            0
        ),navOptions)
    }

    fun emailEditClick(){
//        navigate(SettingsFragmentDirections.actionSettingsFragmentToSettingsEditFragment(
//            username = "บานาน่า ฟิม",
//            email = "banana@kmitl.ac.th",
//            faculty = "คณะวิศวกรรมศาสตร์",
//            department = "สาขาวิศวกรรมคอมพิวเตอร์",
//            year = "4",
//            1
//        ),navOptions)
    }

    fun facultyEditClick(){
        navigate(SettingsFragmentDirections.actionSettingsFragmentToSettingsEditFragment(
            username = _settingsUserData.value?.username,
            email = _settingsUserData.value?.email ?:"",
            faculty = _settingsUserData.value?.faculty ?:"",
            department = _settingsUserData.value?.department ?:"",
            year = _settingsUserData.value?.year ?:"",
            2
        ),navOptions)
    }

    fun departmentEditClick(){
        navigate(SettingsFragmentDirections.actionSettingsFragmentToSettingsEditFragment(
            username = _settingsUserData.value?.username,
            email = _settingsUserData.value?.email ?:"",
            faculty = _settingsUserData.value?.faculty ?:"",
            department = _settingsUserData.value?.department ?:"",
            year = _settingsUserData.value?.year ?:"",
            3
        ),navOptions)
    }

    fun yearEditClick(){
        navigate(SettingsFragmentDirections.actionSettingsFragmentToSettingsEditFragment(
            username = _settingsUserData.value?.username,
            email = _settingsUserData.value?.email ?:"",
            faculty = _settingsUserData.value?.faculty ?:"",
            department = _settingsUserData.value?.department ?:"",
            year = _settingsUserData.value?.year ?:"",
            4
        ),navOptions)
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