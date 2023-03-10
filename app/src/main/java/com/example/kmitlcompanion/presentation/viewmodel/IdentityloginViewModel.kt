package com.example.kmitlcompanion.presentation.viewmodel

import android.app.Activity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.databinding.FragmentIdentityloginBinding
import com.example.kmitlcompanion.domain.usecases.GetUserRoom
import com.example.kmitlcompanion.domain.usecases.PostUserData
import com.example.kmitlcompanion.presentation.BaseViewModel
import com.example.kmitlcompanion.presentation.eventobserver.Event
import com.example.kmitlcompanion.presentation.utils.SingleLiveData
import com.example.kmitlcompanion.ui.identitylogin.IdentityloginFragmentDirections
import com.example.kmitlcompanion.ui.identitylogin.DataFacultyDepart
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.observers.DisposableObserver
import javax.inject.Inject


@HiltViewModel
class IdentityloginViewModel @Inject constructor(
    private val postUserData: PostUserData,
    private val getUser: GetUserRoom,
    //private val dataFacultyDepart: DataFacultyDepart,
) : BaseViewModel() {

    //save data to db
    private val _saveUserDataResponse = SingleLiveData<ArrayList<Any>>()
    val saveUserDataResponse: SingleLiveData<ArrayList<Any>> = _saveUserDataResponse

    //naviagte to homepage
//    private val _nextHomePage = SingleLiveData<Event<Boolean>>()
//    val nextHomepage: SingleLiveData<Event<Boolean>> = _nextHomePage

    //private val dataFacultyDepart = DataFacultyDepart()

    fun saveData(name: String,
                 surname : String,
                 faculty : String,
                 department : String,
                 year : String){

        val arr = arrayListOf<Any>(name,surname,faculty,department,year,"")
        Log.d("sendArrayData",arr.toString())

        //getUserToken and SendArrayData in oncomplete
        getUser.execute(object : DisposableObserver<List<UserData>>(){
            override fun onComplete() {
                Log.d("getUserRoom","Success !!!")
            }

            override fun onNext(t: List<UserData>) {
                arr[5] = t[0].token
                _saveUserDataResponse.value = arr
                Log.d("getUserRoom","onNext Success !!!")
            }

            override fun onError(e: Throwable) {
                Log.d("getUserRoom",e.toString())
            }
        })
    }

    //API
    fun postUserData(arr : ArrayList<Any>?){
        Log.d("test_identity",arr.toString())
        postUserData.execute(object : DisposableCompletableObserver() {
            override fun onComplete() {
                Log.d("QUERY DATA","Success !!!")
                //_nextHomePage.value = Event(true)
                navigate(IdentityloginFragmentDirections.actionIdentityloginFragmentToMapboxFragment2())
            }
            override fun onError(e: Throwable) {
                Log.d("QUERY DATA",e.toString())
            }
        }, arr)
    }

//    fun getUserRoom(arr : ArrayList<Any>){
//        getUser.execute(object : DisposableObserver<List<UserData>>(){
//            override fun onComplete() {
//                Log.d("getUserRoom","Success !!!")
//            }
//
//            override fun onNext(t: List<UserData>) {
//                arr[5] = t[0].token
//                _saveUserDataResponse.value = arr
//                Log.d("getUserRoom","onNext Success !!!")
//            }
//
//            override fun onError(e: Throwable) {
//                Log.d("getUserRoom",e.toString())
//            }
//        })
//    }


    //Navigation
    fun nextHomePage(){
        navigate(IdentityloginFragmentDirections.actionIdentityloginFragmentToMapboxFragment2())
    }


}