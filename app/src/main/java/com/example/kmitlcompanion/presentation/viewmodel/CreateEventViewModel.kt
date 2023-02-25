package com.example.kmitlcompanion.presentation.viewmodel

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kmitlcompanion.domain.model.EventDetail
import com.example.kmitlcompanion.domain.usecases.CreateEventQuery
import com.example.kmitlcompanion.presentation.BaseViewModel
import com.example.kmitlcompanion.presentation.eventobserver.Event
import com.example.kmitlcompanion.ui.createevent.CreateEventFragmentDirections
import com.example.kmitlcompanion.ui.mapboxview.utils.DateUtils
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel  @Inject constructor(
    private val createEventQuery: CreateEventQuery,
) : BaseViewModel(){


    private val _currentLocation = MutableLiveData<EventDetail>()
    val currentLocation: LiveData<EventDetail> = _currentLocation

    private val _imageUpload = MutableLiveData<Event<Boolean>>()
    val imageUpload : LiveData<Event<Boolean>> = _imageUpload

    private val _discardImage = MutableLiveData<Event<Boolean>>()
    val discardImage : LiveData<Event<Boolean>> = _discardImage

    private val _imageData = MutableLiveData<Intent?>()
    val imageData : LiveData<Intent?> = _imageData
    private val _storeImageData : MutableList<Intent?> = mutableListOf()


    private val _nameInput = MutableLiveData<String>()
    val nameInput : LiveData<String> = _nameInput

    private val _detailInput = MutableLiveData<String>()
    val detailInput : LiveData<String> = _detailInput

    private val _privateUpload = MutableLiveData<Event<Boolean>>()
    val privateUpload : LiveData<Event<Boolean>> = _privateUpload

    private val _publicUpload = MutableLiveData<Event<Boolean>>()
    val publicUpload : LiveData<Event<Boolean>> = _publicUpload

    //For DateTime
    private val _startDateTimePickEvent = MutableLiveData<Event<Boolean>>()
    val startDateTimePickEvent : LiveData<Event<Boolean>> = _startDateTimePickEvent

    private val _startDateTimeValue = MutableLiveData<Calendar>()
    val startDateTimeValue : LiveData<Calendar> = _startDateTimeValue

    private val _endDateTimePickEvent = MutableLiveData<Event<Boolean>>()
    val endDateTimePickEvent : LiveData<Event<Boolean>> = _endDateTimePickEvent

    private val _endDateTimeValue = MutableLiveData<Calendar>()
    val endDateTimeValue : LiveData<Calendar> = _endDateTimeValue

    private val _uploadLoading = MutableLiveData<Boolean>()
    val uploadLoading : MutableLiveData<Boolean> = _uploadLoading

    fun updateNameInput(name: String?) {
        _nameInput.value = name?:""
    }
    fun updateDetailInput(name : String?) {
        _detailInput.value = name?:""
    }

    fun updateImage(intent: Intent) {
        Log.d("nulllcheck","viewModel")
        _imageData.value = intent
        _storeImageData.add(_imageData.value)
    }

    fun removeImage(){
        //_imageData.value!!.removeLast()
        //_imageData.value = null
        _storeImageData.removeLast()
    }

    fun uploadImage() {
        _imageUpload.value = Event(true)
    }

    fun discardImage() {
        _discardImage.value = Event(true)
    }

    fun updateCurrentLocation(eventDetail: EventDetail){
        _currentLocation.value = eventDetail
    }

    fun clickPrivateUpload() {
        _privateUpload.value = Event(true)
    }

    fun clickPublicUpload() {
        _publicUpload.value = Event(true)
    }

    fun privateLocation() {
        var sTime = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(_startDateTimeValue.value?.time)
        var eTime = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(_endDateTimeValue.value?.time)
        var file : MutableList<File?> = mutableListOf()
        var uris : MutableList<Uri?> = mutableListOf()
        _storeImageData.forEach{
            file.add(ImagePicker.getFile(it))
            uris.add(it?.data)
        }
        uploadLoading.value = true
        createEventQuery.execute(object : DisposableCompletableObserver() {

            override fun onComplete() {
                Log.d("test_createEventQuery","trye")
                goToMapbox()

            }

            override fun onError(e: Throwable) {
                Log.d("createEventQuery","Failed")
                goToMapbox()
            }

        }, Triple(com.example.kmitlcompanion.domain.model.Event(
            name = nameInput.value,
            description = detailInput.value,
            startTime = sTime,
            endTime = eTime,
            point = currentLocation.value!!.point,
            file = file,
            uri = uris
        ),sTime,eTime)
        )
    }

    fun publicLocation(){
        var file : MutableList<File?> = mutableListOf()
        var uris : MutableList<Uri?> = mutableListOf()
        _storeImageData.forEach{
            file.add(ImagePicker.getFile(it))
            uris.add(it?.data)
        }

        /*createPublicLocationQuery.execute(object : DisposableCompletableObserver() {
            override fun onComplete() {
                goToMapbox()
            }
            override fun onError(e: Throwable) {
                Log.d("image_debug","Failed")
            }
        }, LocationPublic(
            inputName = nameInput.value,
            description = detailInput.value,
            place = currentLocation.value!!.place,
            type = typeSpinner.value,
            address = currentLocation.value!!.address,
            point = currentLocation.value!!.point,
            file = file,
            uri = uris
        )
        )*/
    }

    fun startDateTimePicker() {
        _startDateTimePickEvent.value = Event(true)
    }

    fun getStartDateTimePick(cal : Calendar){
        _startDateTimeValue.value = cal
        Log.d("test_datetime",cal.toString())
    }

    fun endDateTimePicker() {
        _endDateTimePickEvent.value = Event(true)
    }

    fun getEndDateTimePick(cal : Calendar){
        _endDateTimeValue.value = cal
        Log.d("test_datetime",cal.toString())
    }


    //Navigation
    fun goToMapbox() {
        navigate(CreateEventFragmentDirections.actionCreateEventFragmentToMapboxFragment())
    }
}