package com.example.kmitlcompanion.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.kmitlcompanion.domain.model.NotiLogDetails
import com.example.kmitlcompanion.domain.usecases.GetNotificationLogDetails
import com.example.kmitlcompanion.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.observers.DisposableObserver
import javax.inject.Inject

@HiltViewModel
class NotiLogPageViewModel @Inject constructor(
    val getNotificationLogDetails: GetNotificationLogDetails
) : BaseViewModel() {

    private val _notiLogData = MutableLiveData<MutableList<NotiLogDetails>>()
    val notiLogData: LiveData<MutableList<NotiLogDetails>> = _notiLogData


    fun downloadNotiLog(){

        getNotificationLogDetails.execute(object : DisposableObserver<List<NotiLogDetails>>(){
            override fun onNext(t: List<NotiLogDetails>) {
                _notiLogData.value = t as MutableList<NotiLogDetails>
            }

            override fun onError(e: Throwable) {
                Log.d("getNotificationLogDetails","$e")
            }

            override fun onComplete() {
                Log.d("getNotificationLogDetails","onComplete")
            }
        })
    }


}