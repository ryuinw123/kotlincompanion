package com.example.kmitlcompanion.presentation.viewmodel

import android.util.Log
import com.example.kmitlcompanion.data.model.ReturnLoginData
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.domain.usecases.UpdateUser
import com.example.kmitlcompanion.domain.usecases.getUserRoom
import com.example.kmitlcompanion.domain.usecases.postLogin
import com.example.kmitlcompanion.presentation.BaseViewModel
import com.example.kmitlcompanion.presentation.eventobserver.Event
import com.example.kmitlcompanion.presentation.utils.SingleLiveData
import com.example.kmitlcompanion.ui.startpage.StartPageFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.observers.DisposableObserver

import javax.inject.Inject

@HiltViewModel
class StartPageViewModel @Inject constructor(
    private val postLogin: postLogin,
    private val updateUser: UpdateUser,
    private val getUser: getUserRoom
)
    : BaseViewModel() {

    //getuser room
    private val _getUserRoom = SingleLiveData<Event<Boolean>>()
    val getUserRoom: SingleLiveData<Event<Boolean>> = _getUserRoom

    //login
    private val _loginResponse = SingleLiveData<String>()
    val loginResponse: SingleLiveData<String> = _loginResponse

    //update user data in room
    private val _updateUserRoom = SingleLiveData<UserData>()
    val updateUserRoom: SingleLiveData<UserData> = _updateUserRoom

    fun loginWithToken(){
        //get user token and login
        _getUserRoom.value = Event(true)

    }

    fun postLogin(authCode : String){
        postLogin.execute(object : DisposableObserver<ReturnLoginData>() {
            override fun onComplete() {
                Log.d("AUTH","Success")
            }
            override fun onNext(t: ReturnLoginData) {
                if (t.status == 1){
                    //goto homepage
                    navigate(StartPageFragmentDirections.actionStartPageToMapboxFragment2())
                }else if(t.status == 0){
                    //goto login page
                    navigate(StartPageFragmentDirections.actionStartPageToLoginFragment())
                    _updateUserRoom.value = UserData(id=0,email=t.email,token=t.refreshToken)
                }
            }
            override fun onError(e: Throwable) {
                navigate(StartPageFragmentDirections.actionStartPageToLoginFragment())
                _updateUserRoom.value = UserData(id=0,email="",token="")
                Log.d("AUTH",e.toString())
            }
        }, authCode)
    }

    fun getUserRoom(){
        getUser.execute(object : DisposableObserver<List<UserData>>(){
            override fun onComplete() {
                Log.d("getUserRoom","Success !!!")
            }

            override fun onNext(t: List<UserData>) {

                if (!(t.isNullOrEmpty())){
                    Log.d("printUserRoom",t[0].toString())
                    _loginResponse.value = t[0].token
                }else{
                    _loginResponse.value = ""
                }

                Log.d("getUserRoom","onNext Success !!!")
            }

            override fun onError(e: Throwable) {
                Log.d("getUserRoom",e.toString())
            }
        })
    }

    fun updateUser(userData: UserData) {
        updateUser.execute(object : DisposableCompletableObserver(){
            override fun onComplete() {
                Log.d("UpdateUser","Update UserData Complete!!!")
            }

            override fun onError(e: Throwable) {
                Log.d("UpdateUser",e.toString())
            }
        },userData)
    }

}