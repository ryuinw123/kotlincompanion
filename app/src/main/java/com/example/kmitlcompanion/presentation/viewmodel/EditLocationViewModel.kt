package com.example.kmitlcompanion.presentation.viewmodel

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kmitlcompanion.domain.model.*
import com.example.kmitlcompanion.domain.usecases.EditLocationQuery
import com.example.kmitlcompanion.presentation.BaseViewModel
import com.example.kmitlcompanion.presentation.eventobserver.Event
import com.example.kmitlcompanion.ui.createlocation.CreateLocationFragmentDirections
import com.example.kmitlcompanion.ui.editlocation.EditLocationFragmentDirections
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditLocationViewModel @Inject constructor(
    private val editLocationQuery: EditLocationQuery
) : BaseViewModel() {

    private val _editLocationResponse = MutableLiveData<Boolean>()
    val editLocationResponse: MutableLiveData<Boolean> = _editLocationResponse

    private val _imageUpload = MutableLiveData<Event<Boolean>>()
    val imageUpload : LiveData<Event<Boolean>> = _imageUpload

    private val _discardImage = MutableLiveData<Event<Boolean>>()
    val discardImage : LiveData<Event<Boolean>> = _discardImage

    private val _imageData = MutableLiveData<Intent?>()
    val imageData : LiveData<Intent?> = _imageData

    private val _storeImageData : MutableList<Pair<Int,Any>?> = mutableListOf()

    private val _markerId = MutableLiveData<String?>()
    val markerId : LiveData<String?> = _markerId

    private val _nameInput = MutableLiveData<String>()
    val nameInput : LiveData<String> = _nameInput

    private val _detailInput = MutableLiveData<String>()
    val detailInput : LiveData<String> = _detailInput

    private val _typeSpinner = MutableLiveData<String>()
    val typeSpinner : LiveData<String> = _typeSpinner

    private val _setStartImageUrl = MutableLiveData<MutableList<String>?>()
    val setStartImageUrl: MutableLiveData<MutableList<String>?> = _setStartImageUrl

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
        _storeImageData.add(Pair(0,_imageData.value!!))
    }

    fun removeImage(){
        _storeImageData.removeLast()
    }

    fun updateTypeSpinner(type : String) {
        _typeSpinner.value = type
    }

    fun uploadImage() {
        _imageUpload.value = Event(true)
    }

    fun discardImage() {
        _discardImage.value = Event(true)
    }

    fun updateId(id : String?){
        _markerId.value = id
    }

    fun editUploadLocation(){
        _uploadLoading.value = true
        editLocationQuery.execute(object : DisposableCompletableObserver() {
            override fun onComplete() {
                goToMapbox()
            }

            override fun onError(e: Throwable) {
                goToMapbox()
                Log.d("editLocationQuery",e.toString())
            }
        },params = EditLocation(
            id = _markerId.value,
            name = _nameInput.value,
            type = _typeSpinner.value,
            description = _detailInput.value,
            image = _storeImageData,
        ))

        Log.d("test_edit","updateLocation" + _markerId.value + " " + _nameInput.value + " " + _detailInput.value + " " + _typeSpinner.value + " " + _storeImageData.toString())
    }

    fun setStartImage(url : MutableList<String>?){
        url?.forEach {
            _storeImageData.add(Pair(1,it))
        }
        _setStartImageUrl.value = url
    }

    //Navigation
    fun goToMapbox() {
        navigate(EditLocationFragmentDirections.actionEditLocationFragmentToMapboxFragment())
    }
}