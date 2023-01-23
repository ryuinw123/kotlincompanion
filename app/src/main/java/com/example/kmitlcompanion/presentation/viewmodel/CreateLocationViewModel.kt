package com.example.kmitlcompanion.presentation.viewmodel

import android.content.Intent
import android.net.Uri
import android.text.BoringLayout
import android.util.Log
import android.widget.Spinner
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kmitlcompanion.domain.model.Location
import com.example.kmitlcompanion.domain.model.LocationDetail
import com.example.kmitlcompanion.domain.usecases.CreateLocationQuery
import com.example.kmitlcompanion.presentation.BaseViewModel
import com.example.kmitlcompanion.presentation.eventobserver.Event
import com.example.kmitlcompanion.ui.createlocation.CreateLocation
import com.example.kmitlcompanion.ui.createlocation.CreateLocationDirections
import com.example.kmitlcompanion.ui.mapboxview.MapboxFragmentDirections
import com.github.dhaval2404.imagepicker.ImagePicker
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.internal.operators.completable.CompletableObserveOn
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import java.io.File
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class CreateLocationViewModel @Inject constructor(
    private val createLocationQuery: CreateLocationQuery
) : BaseViewModel(){


    private val _currentLocation = MutableLiveData<LocationDetail>()
    val currentLocation: LiveData<LocationDetail> = _currentLocation

    private val _imageUpload = MutableLiveData<Event<Boolean>>()
    val imageUpload : LiveData<Event<Boolean>> = _imageUpload

    private val _discradImage = MutableLiveData<Event<Boolean>>()
    val discradImage : LiveData<Event<Boolean>> = _discradImage

    private val _imageData = MutableLiveData<Intent?>()
    val imageData : LiveData<Intent?> = _imageData
    private val _storeImageData : MutableList<Intent?> = mutableListOf()


    private val _nameInput = MutableLiveData<String>()
    val nameInput : LiveData<String> = _nameInput

    private val _detailInput = MutableLiveData<String>()
    val detailInput : LiveData<String> = _detailInput



    private val _typeSpinner = MutableLiveData<String>()
    val typeSpinner : LiveData<String> = _typeSpinner

    private val _privateUpload = MutableLiveData<Event<Boolean>>()
    val privateUpload : LiveData<Event<Boolean>> = _privateUpload

    private val _publicUpload = MutableLiveData<Event<Boolean>>()
    val publicUpload : LiveData<Event<Boolean>>  = _publicUpload

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
        //_imageData.value?.add(intent)
    }

    fun removeImage(){
        //_imageData.value!!.removeLast()
        //_imageData.value = null
        _storeImageData.removeLast()
    }

    fun updateTypeSpinner(type : String) {
        _typeSpinner.value = type
    }

    fun uploadImage() {
        _imageUpload.value = Event(true)
    }

    fun discardImage() {
        _discradImage.value = Event(true)
    }

    fun updateCurrentLocation(locationDetail: LocationDetail){
        _currentLocation.value = locationDetail
    }

    fun clickPrivateUpload() {
        _privateUpload.value = Event(true)
    }

    fun clickPublicUpload() {
        _publicUpload.value = Event(true)
    }

    fun createLocation() {
        var file : MutableList<File?> = mutableListOf()
        var uris : MutableList<Uri?> = mutableListOf()
        _storeImageData.forEach{
            file.add(ImagePicker.getFile(it))
            uris.add(it?.data)
            //Log.d("image_debug",file.last().toString())
        }
        Log.d("nulllcheck",file.toString())
        Log.d("nulllcheck",uris.toString())
       //val file = ImagePicker.getFile(imageData.value)
        //Log.d("image_debug",imageData.value.toString())


        createLocationQuery.execute(object : DisposableCompletableObserver() {
            override fun onComplete() {
                goToMapbox()
            }

            override fun onError(e: Throwable) {
                Log.d("image_debug","Failed")
            }

        },Location(
            inputName = nameInput.value,
            description = detailInput.value,
            place = currentLocation.value!!.place,
            type = typeSpinner.value,
            address = currentLocation.value!!.address,
            point = currentLocation.value!!.point,
            file = file,
            uri = uris
            )
        )
    }

    //Navigation
    fun goToMapbox() {
        navigate(CreateLocationDirections.actionCreateLocationToMapboxFragment2())
    }

}