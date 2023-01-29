package com.example.kmitlcompanion.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kmitlcompanion.domain.model.Comment
import com.example.kmitlcompanion.domain.model.LikeDetail
import com.example.kmitlcompanion.domain.model.MapInformation
import com.example.kmitlcompanion.domain.usecases.GetMapLocations
import com.example.kmitlcompanion.domain.usecases.GetPinDetailsLocationQuery
import com.example.kmitlcompanion.domain.usecases.addLikeLocationQuery
import com.example.kmitlcompanion.domain.usecases.removeLikeLocationQuery
import com.example.kmitlcompanion.presentation.BaseViewModel
import com.example.kmitlcompanion.presentation.eventobserver.Event
import com.example.kmitlcompanion.ui.mapboxview.MapboxFragmentDirections
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.observers.DisposableObserver
import javax.inject.Inject

@HiltViewModel
class MapboxViewModel @Inject constructor(
    private val getMapLocations: GetMapLocations,
    private val getPinDetailsLocationQuery: GetPinDetailsLocationQuery,
    private val addLikeLocationQuery: addLikeLocationQuery,
    private val removeLikeLocationQuery: removeLikeLocationQuery
) : BaseViewModel() {

    private val _mapInformationResponse = MutableLiveData<MapInformation>()
    val mapInformationResponse: LiveData<MapInformation> = _mapInformationResponse

    private val _bottomSheetState = MutableLiveData<Int>()
    val bottomSheetState: LiveData<Int> = _bottomSheetState

    private val _currentLocationGps = MutableLiveData<String?>()
    val currentLocationGps: LiveData<String?> = _currentLocationGps

    private val _idLocationLabel = MutableLiveData<String?>()
    val idLocationLabel : LiveData<String?> = _idLocationLabel

    private val _nameLocationLabel = MutableLiveData<String?>()
    val nameLocationLabel : LiveData<String?> = _nameLocationLabel

    private val _placeLocationLabel = MutableLiveData<String?>()
    val placeLocationLabel : LiveData<String?> = _placeLocationLabel

    private val _addressLocationLabel = MutableLiveData<String?>()
    val addressLocationLabel : LiveData<String?> = _addressLocationLabel

    private val _descriptionLocationLabel = MutableLiveData<String?>()
    val descriptionLocationLabel : LiveData<String?> = _descriptionLocationLabel

    private val _likeCoutingUpdate = MutableLiveData<Int?>()
    val likeCoutingUpdate : LiveData<Int?> = _likeCoutingUpdate

    private val _onClicklikeLocation = MutableLiveData<Event<Boolean>>()
    val onClicklikeLocation : LiveData<Event<Boolean>> = _onClicklikeLocation

    private val _isLiked = MutableLiveData<Boolean?>()
    val isLiked : LiveData<Boolean?> = _isLiked

    private val _positionFlyer = MutableLiveData<Point>()
    val positionFlyer: LiveData<Point> = _positionFlyer

    private val _imageLink = MutableLiveData<List<String>?>()
    val imageLink : LiveData<List<String>?> = _imageLink

    private val _permissionGrand = MutableLiveData(false)
    val permissionGrand : LiveData<Boolean> = _permissionGrand

    private val _locationIcon = MutableLiveData<String>()
    val locationIcon : LiveData<String> = _locationIcon




    //For Navigation

    private val _userLocation = MutableLiveData<Point?>()
    val userLocation : LiveData<Point?> = _userLocation

    private val _navigationEvent = MutableLiveData<Event<Boolean>>()
    val navigationEvent : LiveData<Event<Boolean>> = _navigationEvent

    private val _stopNavigationEvent =  MutableLiveData<Event<Boolean>>()
    val stopNavigationEvent : LiveData<Event<Boolean>> = _stopNavigationEvent

    private val _recenterEvent = MutableLiveData<Event<Boolean>>()
    val recenterEvent : LiveData<Event<Boolean>> = _recenterEvent

    private val _routeOverviewEvent = MutableLiveData<Event<Boolean>>()
    val routeOverViewEvent : LiveData<Event<Boolean>> = _routeOverviewEvent

    private val _soundEvent = MutableLiveData<Event<Boolean>>()
    val soundEvent : LiveData<Event<Boolean>> = _soundEvent


    fun updateStopNavigationEvent() {
        _stopNavigationEvent.value = Event(true)
    }
    fun updateRecenterEvent() {
        _recenterEvent.value = Event(true)
    }
    fun updateRouteOverviewEvent() {
        _routeOverviewEvent.value = Event(true)
    }
    fun updateSoundEvent() {
        _soundEvent.value = Event(true)
    }

    fun updateLocationIcon(icon : String) {
        _locationIcon.value = icon
    }







    fun downloadLocations() {
        getMapLocations.execute(object : DisposableObserver<MapInformation>() {
            override fun onComplete() {
                println("Complete")
            }

            override fun onNext(t: MapInformation) {
                println("FromObserver")
                println(t)
                _mapInformationResponse.value = t
            }

            override fun onError(e: Throwable) {
                println("Error")
            }

        })
    }

    fun getLikeLocationQuery(id : String?){
        getPinDetailsLocationQuery.execute(object : DisposableObserver<LikeDetail>() {

            override fun onComplete() {
                Log.d("GetPinDetailsLocationQuery","complete")

            }

            override fun onNext(t: LikeDetail) {
                Log.d("GetPinDetailsLocationQuery","onNext")
                _likeCoutingUpdate.value = t.likeCounting
                _isLiked.value = t.isLiked
            }

            override fun onError(e: Throwable) {
                Log.d("GetPinDetailsLocationQuery",e.toString())
            }

        }, params = id)
    }

    fun addLikeLocationQuery(id : String?){
        addLikeLocationQuery.execute(object : DisposableCompletableObserver(){
            override fun onComplete() {
                _likeCoutingUpdate.value = 1 + _likeCoutingUpdate.value!!
                _isLiked.value = true
            }

            override fun onError(e: Throwable) {
                Log.d("addLikeLocationQuery",e.toString())
            }
        }, params = id)
    }

    fun removeLikeLocationQuery(id : String?){
        removeLikeLocationQuery.execute(object : DisposableCompletableObserver(){
            override fun onComplete() {
                _likeCoutingUpdate.value = _likeCoutingUpdate.value!! - 1
                _isLiked.value = false
            }

            override fun onError(e: Throwable) {
                Log.d("addLikeLocationQuery",e.toString())
            }
        }, params = id)
    }

    fun onClickLikeLocationQuery() {
        _onClicklikeLocation.value = Event(true)

    }
    
    fun updateUserLocation(point: Point) {
        _userLocation.value = point
    }

    fun updatePermission(boolean: Boolean) {
        _permissionGrand.value = boolean
    }

    fun updateNavigationEvent() {
        _navigationEvent.value = Event(true)
    }

    fun updateImageLink(imageList : List<String>) {
        _imageLink.value = imageList
    }

    fun updateBottomSheetState(state: Int) {
        _bottomSheetState.value = state
    }

    fun updateCurrentLocationGps(point: Point){
        val lat = point.latitude()
        val long = point.longitude()
        _currentLocationGps.value = "Lat: $lat, Long: $long"
    }
    fun updateIdLocationLabel(id : String){
        _idLocationLabel.value = "$id"
    }
    fun updateNameLocationLabel(name : String){
        _nameLocationLabel.value = "$name"
    }

    fun updatePlaceLocationLabel(place : String){
        _placeLocationLabel.value = "$place"
    }

    fun updateAddressLocationLabel(address : String){
        _addressLocationLabel.value = "$address"
    }

    fun updateDescriptionLocationLabel(description : String) {
        _descriptionLocationLabel.value = "$description"
    }

    fun updatePositionFlyer(point: Point) {
        _positionFlyer.value = point
    }


    fun goToCreateMapBox(){
        navigate(MapboxFragmentDirections.actionMapboxFragment2ToCreateMapboxLocationFragment2())
    }

    fun goBackClicked() {
        navigateBack()
    }


    private val _commentList = MutableLiveData(mutableListOf(
        Comment(1, "15/05/2021 at 12:20", "Anonymous1", "Comment1"),
        Comment(2, "15/05/2021 at 12:22", "Anonymous2", "Comment2"),
        Comment(3, "15/05/2021 at 12:23", "Anonymous3", "Comment3"),
        Comment(4, "15/05/2021 at 12:24", "Anonymous4", "Comment4"),
    ))
    val commentList: LiveData<MutableList<Comment>> = _commentList

    fun addComment(shortComment: Comment) {
        val newList = _commentList.value ?: mutableListOf()
        newList.add(shortComment)
        _commentList.value = newList
    }

}