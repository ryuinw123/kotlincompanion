package com.example.kmitlcompanion.presentation.viewmodel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kmitlcompanion.domain.model.EditEventLocation
import com.example.kmitlcompanion.domain.usecases.EditEventQuery
import com.example.kmitlcompanion.presentation.BaseViewModel
import com.example.kmitlcompanion.presentation.eventobserver.Event
import com.example.kmitlcompanion.ui.createevent.utils.EventTypeUtils
import com.example.kmitlcompanion.ui.editevent.EditEventFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import javax.inject.Inject

@HiltViewModel
class EditEventViewModel @Inject constructor(
    private val editEventQuery: EditEventQuery,
    private val eventTypeUtils: EventTypeUtils,
) : BaseViewModel() {

    private val _editEventResponse = MutableLiveData<Boolean>()
    val editEventResponse: MutableLiveData<Boolean> = _editEventResponse

    private val _imageUpload = MutableLiveData<Event<Boolean>>()
    val imageUpload : LiveData<Event<Boolean>> = _imageUpload

    private val _discardImage = MutableLiveData<Event<Boolean>>()
    val discardImage : LiveData<Event<Boolean>> = _discardImage

    private val _imageData = MutableLiveData<Intent?>()
    val imageData : LiveData<Intent?> = _imageData

    private val _storeImageData : MutableList<Pair<Int,Any>?> = mutableListOf()

    private val _eventId = MutableLiveData<String?>()
    val eventId : LiveData<String?> = _eventId

    private val _nameInput = MutableLiveData<String>()
    val nameInput : LiveData<String> = _nameInput

    private val _detailInput = MutableLiveData<String>()
    val detailInput : LiveData<String> = _detailInput

    private val _setStartImageUrl = MutableLiveData<MutableList<String>>()
    val setStartImageUrl: MutableLiveData<MutableList<String>> = _setStartImageUrl

    //For DateTime
//    private val _startDateTimePickEvent = MutableLiveData<Event<Boolean>>()
//    val startDateTimePickEvent : LiveData<Event<Boolean>> = _startDateTimePickEvent
//
    private val _startDateTimeValue = MutableLiveData<String>()
    val startDateTimeValue : LiveData<String> = _startDateTimeValue
//
//    private val _endDateTimePickEvent = MutableLiveData<Event<Boolean>>()
//    val endDateTimePickEvent : LiveData<Event<Boolean>> = _endDateTimePickEvent
//
    private val _endDateTimeValue = MutableLiveData<String>()
    val endDateTimeValue : LiveData<String> = _endDateTimeValue

    private val _uploadLoading = MutableLiveData<Boolean>()
    val uploadLoading : MutableLiveData<Boolean> = _uploadLoading

    //For event Type
    private val _eventType = MutableLiveData<String>()
    val eventType : LiveData<String> = _eventType

    private val _eventUrl = MutableLiveData<String>()
    val eventUrl : LiveData<String> = _eventUrl


    fun updateNameInput(name: String?) {
        _nameInput.value = name?:""
    }

    fun updateDetailInput(name : String?) {
        _detailInput.value = name?:""
    }

    fun updateImage(intent: Intent) {
        Log.d("nulllcheck","viewModel")
        _imageData.value = intent
        _storeImageData.add(Pair(0,_imageData.value!!))
    }

    fun removeImage(){
        _storeImageData.removeLast()
    }

    fun uploadImage() {
        _imageUpload.value = Event(true)
    }

    fun discardImage() {
        _discardImage.value = Event(true)
    }

    fun updateId(id : String?){
        _eventId.value = id
    }

    fun editUploadLocation(){
        _uploadLoading.value = true
        editEventQuery.execute(object : DisposableCompletableObserver() {
            override fun onComplete() {
                goToMapbox()
            }

            override fun onError(e: Throwable) {
                goToMapbox()
                Log.d("editEventQuery",e.toString())
            }
        }, params = EditEventLocation(
            eventId = _eventId.value,
            name = _nameInput.value,
            description = _detailInput.value,
            image = _storeImageData,
            type = eventTypeUtils.getCodeByType(_eventType.value!!),
            url = _eventUrl.value,
        ))
        Log.d("test_edit","updateLocation" + _eventId.value + " " + _nameInput.value + " " + _detailInput.value + " " + _storeImageData.toString()
        + " " + _eventType.value + " " +_eventUrl.value)
    }

    fun setStartImage(url : MutableList<String>){
        url.forEach {
            _storeImageData.add(Pair(1,it))
        }
        _setStartImageUrl.value = url
    }


    //For Date Time
    fun setStartDateTimePick(cal : String){
        _startDateTimeValue.value = cal
    }

    fun setEndDateTimePick(cal : String){
        _endDateTimeValue.value = cal
    }

    //For event type
    fun updateEventType(type : String){
        _eventType.value = type
    }

    fun updateEventUrl(url : String){
        _eventUrl.value = url
    }

    //Navigation
    fun goToMapbox() {
        navigate(EditEventFragmentDirections.actionEditEventFragmentToMapboxFragment())
    }

}