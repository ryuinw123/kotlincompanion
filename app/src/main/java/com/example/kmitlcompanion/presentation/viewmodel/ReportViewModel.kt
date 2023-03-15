package com.example.kmitlcompanion.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kmitlcompanion.domain.model.MapInformation
import com.example.kmitlcompanion.domain.usecases.ReportEventLocationQueryDetails
import com.example.kmitlcompanion.domain.usecases.ReportMarkerLocationQueryDetails
import com.example.kmitlcompanion.presentation.BaseViewModel
import com.example.kmitlcompanion.presentation.eventobserver.Event
import com.example.kmitlcompanion.ui.report.ReportFragment
import com.example.kmitlcompanion.ui.report.ReportFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    val reportEventLocationQueryDetails: ReportEventLocationQueryDetails,
    val reportMarkerLocationQueryDetails: ReportMarkerLocationQueryDetails,
): BaseViewModel() {

    private val _eventState = MutableLiveData<Event<Boolean>>()
    val eventState: LiveData<Event<Boolean>> = _eventState

    private val _id =  MutableLiveData<Long>()
    val id : LiveData<Long> = _id

    private val _name =  MutableLiveData<String>()
    val name : LiveData<String> = _name

    private val _spinnerType = MutableLiveData<String>()
    val spinnerType : LiveData<String> = _spinnerType

    private val _details =  MutableLiveData<String>("")
    val details : LiveData<String> = _details

    private val _submitReportTrigger = MutableLiveData<Event<Boolean>>()
    val submitReportTrigger : LiveData<Event<Boolean>> = _submitReportTrigger

    fun setEventState(event : Boolean){
        _eventState.value = Event(event)
    }

    fun setId(id : Long){
        _id.value = id
    }

    fun setName(name : String){
        _name.value = name
    }

    fun updateSpinner(data : String){
        _spinnerType.value = data
    }

    fun updateDetails(string: String){
        _details.value = string
    }

    fun onClickSubmitReport(){
        _submitReportTrigger.value = Event(true)
    }

    fun sendReportToMarker(){
        reportMarkerLocationQueryDetails.execute(object : DisposableCompletableObserver() {
            override fun onComplete() {
                goToMapBox()
                Log.d("sendReportToEvent","onComplete")
            }

            override fun onError(e: Throwable) {
                Log.d("sendReportToMarker","$e")
            }
        }, params = Triple(_id.value,_spinnerType.value,_details.value))
    }

    fun sendReportToEvent(){
        reportEventLocationQueryDetails.execute(object : DisposableCompletableObserver() {
            override fun onComplete() {
                goToMapBox()
                Log.d("sendReportToEvent","onComplete")
            }

            override fun onError(e: Throwable) {
                Log.d("sendReportToEvent","$e")
            }
        },params = Triple(_id.value,_spinnerType.value,_details.value))
    }

    fun goToMapBox(){
        navigate(ReportFragmentDirections.actionReportFragmentToMapboxFragment())
    }

    fun goBack(){
        navigateBack()
    }

}