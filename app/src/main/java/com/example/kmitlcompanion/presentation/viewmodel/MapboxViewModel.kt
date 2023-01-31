package com.example.kmitlcompanion.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kmitlcompanion.domain.model.*
import com.example.kmitlcompanion.domain.usecases.*
import com.example.kmitlcompanion.presentation.BaseViewModel
import com.example.kmitlcompanion.presentation.eventobserver.Event
import com.example.kmitlcompanion.ui.mapboxview.MapboxFragmentDirections
import com.example.kmitlcompanion.ui.mapboxview.utils.DateUtils
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.observers.DisposableObserver
import javax.inject.Inject

@HiltViewModel
class MapboxViewModel @Inject constructor(
    private val getMapLocations: GetMapLocations,
    private val dateUtils: DateUtils,
    private val getPinDetailsLocationQuery: GetPinDetailsLocationQuery,
    private val addLikeLocationQuery: addLikeLocationQuery,
    private val removeLikeLocationQuery: removeLikeLocationQuery,
    private val addCommentMarkerLocationQuery: AddCommentMarkerLocationQuery,
    private val editCommentLocationQuery: EditCommentLocationQuery,
    private val deleteCommentLocationQuery: DeleteCommentLocationQuery,
    private val likeDislikeCommentLocationQuery: LikeDislikeCommentLocationQuery
) : BaseViewModel() {

    //For Marker & Location
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

    private val _onClicklikeLocation = MutableLiveData<Boolean?>()
    val onClicklikeLocation : LiveData<Boolean?> = _onClicklikeLocation

    private val _isLiked = MutableLiveData<Boolean>()
    val isLiked : LiveData<Boolean?> = _isLiked

    private val _positionFlyer = MutableLiveData<Point>()
    val positionFlyer: LiveData<Point> = _positionFlyer

    private val _imageLink = MutableLiveData<List<String>?>()
    val imageLink : LiveData<List<String>?> = _imageLink

    //For Comment
    private val _commentList = MutableLiveData<MutableList<Comment>>()
    val commentList: LiveData<MutableList<Comment>> = _commentList

    private val _editComment =  MutableLiveData<Comment>()
    val editComment: MutableLiveData<Comment> = _editComment

    private val _editCommentPosition =  MutableLiveData<Int>()
    //val editCommentPosition: MutableLiveData<Int> = _editCommentPosition

    //For อิหยังนิ
    private val _permissionGrand = MutableLiveData(false)
    val permissionGrand : LiveData<Boolean> = _permissionGrand

    //For Navigation

    private val _userLocation = MutableLiveData<Point?>()
    val userLocation : LiveData<Point?> = _userLocation

    private val _navigationEvent = MutableLiveData<Event<Boolean>>()
    val navigationEvent : LiveData<Event<Boolean>> = _navigationEvent


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

    fun getDetailsLocationQuery(id : String?){
        getPinDetailsLocationQuery.execute(object : DisposableObserver<PinDetail>() {
            override fun onComplete() {
                Log.d("GetPinDetailsLocationQuery","complete")

            }
            override fun onNext(t: PinDetail) {
                _likeCoutingUpdate.value = t.likeCounting
                _isLiked.value = t.isLiked
                _commentList.value = t.comment
                //Log.d("test_pin_vm",t.comment.toString())

            }

            override fun onError(e: Throwable) {
                Log.d("GetPinDetailsLocationQuery",e.toString())
            }

        }, params = id)
    }

    fun addLikeLocationQuery(id : String?){
        _onClicklikeLocation.value = false
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
        _onClicklikeLocation.value = false
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
        _onClicklikeLocation.value = true
    }

    //For Comment
    fun addComment(shortComment: Comment) {
        addCommentMarkerLocationQuery.execute(object : DisposableObserver<ReturnAddComment>(){
            override fun onComplete() {
                Log.d("addCommentMarkerLocationQuery","onComplete")
            }
            override fun onNext(t: ReturnAddComment) {
                Log.d("test_addCommentMarkerLocationQuery", "$t onNext")
                val newList = _commentList.value ?: mutableListOf()
                newList.add(0,Comment(
                    id = t.commentId,
                    date = shortComment.date,
                    author = t.author,
                    message = shortComment.message,
                    like = shortComment.like,
                    dislike = shortComment.dislike,
                    isLikedComment = shortComment.isLikedComment,
                    isDisLikedComment = shortComment.isDisLikedComment,
                    myComment = shortComment.myComment
                ))
                _commentList.value = newList
            }
            override fun onError(e: Throwable) {
                Log.d("addCommentMarkerLocationQuery",e.toString())
            }
        }, params = AddCommentDetail(markerId = _idLocationLabel.value, message = shortComment.message) )
    }

    fun editCommentForm(comment: Comment,position : Int){
        _editCommentPosition.value = position
        _editComment.value = comment
    }

    fun editCommentUpdate(textComment : String){
        //call api
        editCommentLocationQuery.execute(object : DisposableCompletableObserver(){
            override fun onComplete() {
                Log.d("test_editSubmit",_editComment.value?.id.toString() + " " + textComment)
                var newList = _commentList.value ?: mutableListOf()
                var editCommentList = newList[_editCommentPosition.value!!]
                var newComment = Comment(
                    id = editCommentList.id,
                    date = dateUtils.shinGetTime(),
                    author = editCommentList.author,
                    message = textComment,
                    like = editCommentList.like,
                    dislike = editCommentList.dislike,
                    isLikedComment = editCommentList.isLikedComment,
                    isDisLikedComment = editCommentList.isDisLikedComment,
                    myComment = editCommentList.myComment
                )
                newList.removeAt(_editCommentPosition.value!!)
                newList.add(0,newComment)
                _commentList.value = newList
            }
            override fun onError(e: Throwable) {
                Log.d("editCommentLocationQuery",e.toString())
            }
        }, params = Pair(_editComment.value?.id.toString() , textComment))

    }

    fun deleteCommentUpdate(commentId : String, position: Int){
        deleteCommentLocationQuery.execute(object : DisposableCompletableObserver(){
            override fun onComplete() {
                Log.d("test_deleteSubmit",commentId)
                var newList = _commentList.value ?: mutableListOf()
                newList.removeAt(position)
                _commentList.value = newList
            }

            override fun onError(e: Throwable) {
                Log.d("deleteCommentLocationQuery",e.toString())
            }
        }, params = commentId)
    }

    fun likeDisLikeCommentUpdate(comment: Comment,position: Int){
        //commentid
        //isLikedComment
        //isDisLikedComment
        likeDislikeCommentLocationQuery.execute(object : DisposableCompletableObserver(){
            override fun onComplete() {
                var newList = _commentList.value ?: mutableListOf()
                newList[position] = comment
                _commentList.value = newList
                Log.d("test_likedislike",newList.toString())
            }
            override fun onError(e: Throwable) {
                Log.d("likeDislikeCommentLocationQuery",e.toString())
            }
        }, params = Triple(comment.id.toString(),comment.isLikedComment,comment.isDisLikedComment))
    }

    //For ??
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


}