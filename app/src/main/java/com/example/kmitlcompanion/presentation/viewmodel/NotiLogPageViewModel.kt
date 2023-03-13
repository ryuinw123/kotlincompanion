package com.example.kmitlcompanion.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.example.kmitlcompanion.domain.model.NotiLogDetails
import com.example.kmitlcompanion.domain.usecases.DeleteAllNotificationLogDetails
import com.example.kmitlcompanion.domain.usecases.DeleteByIDNotificationLogDetails
import com.example.kmitlcompanion.domain.usecases.GetNotificationLogDetails
import com.example.kmitlcompanion.presentation.BaseViewModel
import com.example.kmitlcompanion.presentation.eventobserver.Event
import com.example.kmitlcompanion.ui.eventpage.NotiLogPageFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.observers.DisposableObserver
import javax.inject.Inject

@HiltViewModel
class NotiLogPageViewModel @Inject constructor(
    val getNotificationLogDetails: GetNotificationLogDetails,
    val deleteByIdNotificationLogDetails: DeleteByIDNotificationLogDetails,
    val deleteAllNotificationLogDetails: DeleteAllNotificationLogDetails,
) : BaseViewModel() {

    private val _notiLogData = MutableLiveData<MutableList<NotiLogDetails>>()
    val notiLogData: LiveData<MutableList<NotiLogDetails>> = _notiLogData

    private val _deleteAllNotiLog = MutableLiveData<Event<Boolean>>()
    val deleteAllNotiLog : LiveData<Event<Boolean>> = _deleteAllNotiLog

    private val _deleteNotiLogByID = MutableLiveData<Long>()
    val deleteNotiLogByID : LiveData<Long> = _deleteNotiLogByID

    private val _goToNoti = MutableLiveData<Pair<Long,Boolean>>()
    val goToNoti : LiveData<Pair<Long,Boolean>> = _goToNoti

    fun removeItem(position:Int) {
        var newList = _notiLogData.value ?: mutableListOf()
        _deleteNotiLogByID.postValue(newList[position].id)
        newList.removeAt(position)
        _notiLogData.postValue(newList)
    }

    fun removeItemByPosting(position:Int) {
        var newList = _notiLogData.value ?: mutableListOf()
        _deleteNotiLogByID.postValue(newList[position].id)
        newList.removeAt(position)
        _notiLogData.postValue(newList)
    }

    fun downloadNotiLog(){
        getNotificationLogDetails.execute(object : DisposableObserver<List<NotiLogDetails>>(){
            override fun onNext(t: List<NotiLogDetails>) {
                _notiLogData.value = t as MutableList<NotiLogDetails>
                Log.d("test_noti","query " + t.toString())
            }

            override fun onError(e: Throwable) {
                Log.d("getNotificationLogDetails","$e")
            }

            override fun onComplete() {
                Log.d("getNotificationLogDetails","onComplete")
            }
        })
    }


    fun deleteNotiLog(){
        _deleteAllNotiLog.value = Event(true)
//        deleteAllNotificationLogDetails.execute(object : DisposableCompletableObserver(){
//            override fun onComplete() {
//                _deleteAllNotiLog.value = Event(true)
//                Log.d("test_noti","delete complete")
//            }
//
//            override fun onError(e: Throwable) {
//                Log.d("deleteAllNotificationLogDetails","$e")
//            }
//        })
    }

    fun deleteNotiLogById(id : Long){
        deleteByIdNotificationLogDetails.execute(object : DisposableCompletableObserver(){
            override fun onComplete() {
                Log.d("test_noti","delete complete")
            }

            override fun onError(e: Throwable) {
                Log.d("test_noti","$e")
            }
        }, params = id)
    }

    fun gotoNotiLogEvent(id : Long,alive : Boolean) {
        _goToNoti.value = Pair(id,alive)
    }

    fun goToMapBox(id : Long){
        navigate(NotiLogPageFragmentDirections.actionNotiLogPageFragmentToMapboxFragment(id = id))
    }

}