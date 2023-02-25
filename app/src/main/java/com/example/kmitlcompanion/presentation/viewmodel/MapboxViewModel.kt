package com.example.kmitlcompanion.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kmitlcompanion.dbomain.usecases.getSearchDetailsQuery
import com.example.kmitlcompanion.domain.model.*
import com.example.kmitlcompanion.domain.usecases.*
import com.example.kmitlcompanion.presentation.BaseViewModel
import com.example.kmitlcompanion.presentation.eventobserver.Event
import com.example.kmitlcompanion.ui.createlocation.utils.TagTypeListUtil
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
    private val getEventLocations: GetEventLocations,
    private val dateUtils: DateUtils,
    private val tagTypeListUtil: TagTypeListUtil,
    private val getPinDetailsLocationQuery: GetPinDetailsLocationQuery,
    private val addLikeLocationQuery: addLikeLocationQuery,
    private val removeLikeLocationQuery: removeLikeLocationQuery,
    private val addCommentMarkerLocationQuery: AddCommentMarkerLocationQuery,
    private val editCommentLocationQuery: EditCommentLocationQuery,
    private val deleteCommentLocationQuery: DeleteCommentLocationQuery,
    private val likeDislikeCommentLocationQuery: LikeDislikeCommentLocationQuery,
    private val getSearchDetailsQuery: getSearchDetailsQuery,
    private val getAllBookmaker: GetAllBookmaker,
    private val updateBookmakerQuery: UpdateBookmakerQuery,
    private val changeEventLikeLocationQuery: ChangeEventLikeLocationQuery,
    private val changeEventBookmarkLocationQuery: ChangeEventBookmarkLocationQuery,
    private val getAllEventBookmaker: GetAllEventBookmaker,
    private val getEventPinDetailsLocationQuery: GetEventPinDetailsLocationQuery,
    private val deleteMarkerLocationQuery: DeleteMarkerLocationQuery,
    private val deleteEventLocationQuery: DeleteEventLocationQuery,
) : BaseViewModel() {

    //For Marker & Location
    private val _mapInformationResponse = MutableLiveData<MapInformation>()
    val mapInformationResponse: LiveData<MapInformation> = _mapInformationResponse

    private val _mapEventResponse = MutableLiveData<EventInformation>()
    val mapEventResponse : LiveData<EventInformation> = _mapEventResponse

    //For reload maps
    private val _refreshMapLocations = MutableLiveData<Boolean>()
    val refreshMapLocations : MutableLiveData<Boolean> = _refreshMapLocations

    private val _refreshEventLocations = MutableLiveData<Boolean>()
    val refreshEventLocations : MutableLiveData<Boolean> = _refreshEventLocations

    private val _bottomSheetState = MutableLiveData<Int>()
    val bottomSheetState: LiveData<Int> = _bottomSheetState

    private val _currentLocationGps = MutableLiveData<String?>()
    val currentLocationGps: LiveData<String?> = _currentLocationGps

    private val _iconLocation = MutableLiveData<Int>()
    val iconLocation : LiveData<Int> = _iconLocation

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

    //For Bookmark
    private val _allMarkerBookmarked = MutableLiveData<MutableList<Int>>()
    val allMarkerBookmarked : LiveData<MutableList<Int>> = _allMarkerBookmarked

    private val _updateBookMark = MutableLiveData<Boolean>()
    val updateBookMark : MutableLiveData<Boolean> = _updateBookMark

    private val _isMarkerBookmarked = MutableLiveData<Boolean>()
    val isMarkerBookmarked : LiveData<Boolean> = _isMarkerBookmarked

    private val _onBookmarkClick = MutableLiveData<Boolean>()
    val onBookmarkClick : MutableLiveData<Boolean> = _onBookmarkClick

    //For อิหยังนิ
    private val _permissionGrand = MutableLiveData(false)
    val permissionGrand : LiveData<Boolean> = _permissionGrand

    private val _locationIcon = MutableLiveData<String>()
    val locationIcon : LiveData<String> = _locationIcon

    //For CreateMenuBottomSheetEventClick


    //BottomEvent

    private val _createAreaEvent = MutableLiveData<Event<Boolean>>()
    val createAreaEvent : LiveData<Event<Boolean>> = _createAreaEvent

    private val _createCircleAreaEvent = MutableLiveData<Event<Boolean>>()
    val createCircleAreaEvent : LiveData<Event<Boolean>> = _createCircleAreaEvent

    private val _createPolygonAreaEvent = MutableLiveData<Event<Boolean>>()
    val createPolygonAreaEvent : LiveData<Event<Boolean>> = _createPolygonAreaEvent

    private val _createPinEvent = MutableLiveData<Event<Boolean>>()
    val createPinEvent : LiveData<Event<Boolean>> = _createPinEvent





    //For Navigation

    private val _userLocation = MutableLiveData<Point?>()
    val userLocation : LiveData<Point?> = _userLocation

    private val _applicationMode = MutableLiveData<Int>()
    val applicationMode : LiveData<Int> = _applicationMode

    private val _recenterEvent = MutableLiveData<Event<Boolean>>()
    val recenterEvent : LiveData<Event<Boolean>> = _recenterEvent

    private val _routeOverviewEvent = MutableLiveData<Event<Boolean>>()
    val routeOverViewEvent : LiveData<Event<Boolean>> = _routeOverviewEvent

    private val _soundEvent = MutableLiveData<Event<Boolean>>()
    val soundEvent : LiveData<Event<Boolean>> = _soundEvent



    //For Search
    private val _appendSearchList = MutableLiveData<MutableList<SearchDetail>>()
    val appendSearchList : LiveData<MutableList<SearchDetail>> = _appendSearchList

    private val _resetSearchList = MutableLiveData<Event<Boolean>>()
    val resetSearchList : LiveData<Event<Boolean>> = _resetSearchList

    private val _submitSearchValue = MutableLiveData<Pair<String?,MutableList<Int?>>>()
    val submitSearchValue : LiveData<Pair<String?,MutableList<Int?>>> = _submitSearchValue

    private val _isSearch = MutableLiveData<Boolean>()
    val isSearch : MutableLiveData<Boolean> = _isSearch

    //For Tag
    private val _itemTagList = MutableLiveData<MutableList<TagViewDataDetail>>()
    val itemTagList : LiveData<MutableList<TagViewDataDetail>> = _itemTagList

    private val _tagFly = MutableLiveData<Pair<Point,Double>>()
    val tagFly : LiveData<Pair<Point,Double>> = _tagFly

    //screen height width
    private val _screenSize = MutableLiveData<ScreenSize>()
    val screenSize : LiveData<ScreenSize> = _screenSize

    //For Event
    private val _eventState = MutableLiveData<Boolean>(false)
    val eventState : LiveData<Boolean> = _eventState

    private val _eventBindStart = MutableLiveData<String>()
    val eventBindStart : LiveData<String> = _eventBindStart

    private val _eventBindEnd = MutableLiveData<String>()
    val eventBindEnd : LiveData<String> = _eventBindEnd

    private val _allEventBookmarkedIdList = MutableLiveData<MutableList<Int>>()
    val allEventBookmarkedIdList : LiveData<MutableList<Int>> = _allEventBookmarkedIdList

    private val _displayTimer = MutableLiveData<String>()
    val displayTimer : MutableLiveData<String> = _displayTimer

    //For Edit Delete Marker

    private val _isMyPin = MutableLiveData<Boolean>()
    val isMyPin : MutableLiveData<Boolean> = _isMyPin

    private val _createdUserName = MutableLiveData<String>()
    val createdUserName : MutableLiveData<String> = _createdUserName

    private val _showMarkerMenuEvent = MutableLiveData<Event<Boolean>>()
    val showMarkerMenuEvent : MutableLiveData<Event<Boolean>> = _showMarkerMenuEvent

    private val _confirmDeleteMarkerDialog = MutableLiveData<Boolean>()
    val confirmDeleteMarkerDialog : MutableLiveData<Boolean> = _confirmDeleteMarkerDialog

    private val _editMarkerTrigger = MutableLiveData<Boolean>()
    val editMarkerTrigger : MutableLiveData<Boolean> = _editMarkerTrigger


    //For ??

    fun updateCreatePinEvent() {
        _createPinEvent.value = Event(true)
    }
    fun updateCreateAreaEvent() {
        _createAreaEvent.value = Event(true)
    }

    fun updateCreateCircleAreaEvent() {
        _createCircleAreaEvent.value = Event(true)
    }

    fun updateCreatePolygonAreaEvent() {
        _createPolygonAreaEvent.value = Event(true)
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

    fun setIconLocation(resource : Int){
        _iconLocation.value = resource
    }


    //For download locations
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

    //For download Event
    fun downloadLocationsEvent() {
        Log.d("test_event","test in event download")
        getEventLocations.execute(object : DisposableObserver<EventInformation>(){

            override fun onComplete() {
                Log.d("test_downloadLocationsEvent","complete")
            }

            override fun onNext(t: EventInformation) {
                Log.d("test_downloadLocationsEvent",t.toString())
                _mapEventResponse.value = t
            }

            override fun onError(e: Throwable) {
                Log.d("test_downloadLocationsEvent",e.toString())
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
                _isMarkerBookmarked.value = t.isBookmarked
                _isMyPin.value = t.isMyPin
                _createdUserName.value = t.createdUserName
                Log.d("test_pin_vm",t.isBookmarked.toString())

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

    //For Bookmark
    fun getAllBookMarker(){
        //excute
        getAllBookmaker.execute(object : DisposableObserver<MutableList<Int>>(){
            override fun onComplete() {
                Log.d("test_getAllBookMarker","on complete")
            }

            override fun onNext(t: MutableList<Int>) {
                _allMarkerBookmarked.value = t
                Log.d("test_getAllBookMarker",t.toString())
            }

            override fun onError(e: Throwable) {
                Log.d("getAllBookMarker",e.toString())
            }
        })
    }

    fun bookMarkOnClick(){
        _onBookmarkClick.value = true
    }

    fun bookMarkUpdate(){//id , status = true mark , false delete mark
        val locationId = _idLocationLabel.value ?:""
        val markerBookmarked = !(_isMarkerBookmarked.value!!) //change bookmark state
        updateBookmakerQuery.execute(object : DisposableCompletableObserver(){
                override fun onComplete() {
                    Log.d("test_eventbookMarkUpdate","complete")
                    _isMarkerBookmarked.value = markerBookmarked
                    _updateBookMark.value = true
                }

                override fun onError(e: Throwable) {
                    Log.d("bookMarkUpdate",e.toString())
                }
            }, params = Pair(locationId,markerBookmarked)
        )
    }



    //For ??
    fun setScreenSize(screenSize: ScreenSize){
        _screenSize.value = screenSize
    }

    fun updateUserLocation(point: Point) {
        _userLocation.value = point
    }

    fun updatePermission(boolean: Boolean) {
        _permissionGrand.value = boolean
    }

    fun updateApplicationMode(mode : Int) {
        _applicationMode.value = mode
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


    //For Search
    fun resetDataToSearchList(){
        _resetSearchList.value = Event(true)
    }

    fun submitSearch(searchText : String?, tagList : MutableList<Int?>){
        _submitSearchValue.value = Pair(searchText,tagList)
    }

    fun appendDataToSearchList(searchText : String?, tagList : MutableList<Int?>){
        val currentLocation = _userLocation.value
        Log.d("test_appendDataToSearchList",_itemTagList.value.toString())
        getSearchDetailsQuery.execute(object : DisposableObserver<List<SearchDetail>>(){
            override fun onComplete() {
                Log.d("getSearchDetailsQuery","onComplete")
            }

            override fun onNext(t: List<SearchDetail>) {
                Log.d("test_getSearchDetailsQuery","onNext $t")
                _appendSearchList.value = t.toMutableList()
            }

            override fun onError(e: Throwable) {
                Log.d("getSearchDetailsQuery_onError",e.toString())
            }
        }, params = Triple(searchText,tagList,currentLocation))
    }

    fun updateSearchStatus(status : Boolean){
        _isSearch.value = status
    }

    //For Tag
    fun updateItemTagList(newTagList : MutableList<TagViewDataDetail>,dummy : TagViewDataDetail){
        newTagList.add(dummy)
        _itemTagList.value = newTagList
    }

    fun flyToTagLocation(posLatlong : Point, zoomLevel : Double){
        _tagFly.value = Pair(posLatlong,zoomLevel)
    }

    fun editItemTagList(newPositionTagList : TagViewDataDetail,position: Int,dummy : TagViewDataDetail){
        var newList = _itemTagList.value ?: mutableListOf<TagViewDataDetail>()
        newList[position] = newPositionTagList
        newList.add(dummy)
        _itemTagList.value = newList
    }

    fun clearItemTag(){
        val dummy = TagViewDataDetail(TagDetail(999999,"x",0), false)
        var newList =   tagTypeListUtil.getAvailableMutableListOfTagTypeDetails().mapIndexed { index, tagDetail ->
                                    TagViewDataDetail(tagDetail, false)
                            }.toMutableList()
        newList.add(dummy)
        _itemTagList.value = newList
    }


    //For Event
    fun getEventDetailsLocationQuery(id : String?){
        getEventPinDetailsLocationQuery.execute(object : DisposableObserver<PinEventDetail>(){
            override fun onNext(t: PinEventDetail) {
                Log.d("getEventPinDetailsLocationQuery",t.toString())
                _likeCoutingUpdate.value = t.eventLikeCounting
                _isLiked.value = t.isEventLiked
                _isMarkerBookmarked.value = t.isEventBookmarked
                _isMyPin.value = t.isMyPin
                _createdUserName.value = t.createdUserName
            }

            override fun onError(e: Throwable) {
                Log.d("getEventPinDetailsLocationQuery",e.toString())
            }

            override fun onComplete() {
                Log.d("getEventPinDetailsLocationQuery","complete")
            }
        }, params = id)
    }

    //get all event bookmark
    fun getAllEventBookMarker(){
        getAllEventBookmaker.execute(object : DisposableObserver<MutableList<Int>>(){
            override fun onNext(t: MutableList<Int>) {
                Log.d("getAllEventBookMarker",t.toString())
                _allEventBookmarkedIdList.value = t
            }

            override fun onError(e: Throwable) {
                Log.d("getAllEventBookMarker",e.toString())
            }

            override fun onComplete() {
                Log.d("getAllEventBookMarker","complete")
            }
        })
    }

    fun changeEventState(state : Boolean){
        _eventState.value = state
    }

    fun setEndTime(endTime : String){
        _eventBindEnd.value = endTime
    }

    fun setStartTime(startTime : String){
        _eventBindStart.value = startTime
    }

    //event like
    fun changeLikeLocationQueryEvent(eventId : String?,isLike : Boolean,updateCountLike : Int){ //true = addlike , false = removelike

        Log.d("test_event_like","$eventId like = $isLike")
        changeEventLikeLocationQuery.execute(object : DisposableCompletableObserver(){
            override fun onComplete() {
                Log.d("test_event_changeLikeLocationQueryEvent","onComplete isLikeEvent")
                _likeCoutingUpdate.value = updateCountLike
                _isLiked.value = isLike
                _onClicklikeLocation.value = false
            }

            override fun onError(e: Throwable) {
                Log.d("test_changeLikeLocationQueryEvent",e.toString())
            }
        }, params = Pair(eventId,isLike))

    }

    //For Event Bookmark
    fun eventBookMarkUpdate(){//id , status = true mark , false delete mark
        val eventId = _idLocationLabel.value ?:""
        val markerBookmarked = !(_isMarkerBookmarked.value ?:false) //change bookmark state
        Log.d("test_eventbookMarkUpdate","event not implement")
        changeEventBookmarkLocationQuery.execute(object : DisposableCompletableObserver(){
            override fun onComplete() {
                Log.d("test_event_changeEventBookmarkLocationQuery",markerBookmarked.toString())
                _isMarkerBookmarked.value = markerBookmarked
                _updateBookMark.value = true
                _onBookmarkClick.value = false

            }

            override fun onError(e: Throwable) {
                Log.d("test_changeLikeLocationQueryEvent",e.toString())
            }
        }, params = Pair(eventId,markerBookmarked))
    }

    fun changeEventTimerEnd(timeValue : String){
        _displayTimer.value = timeValue
    }


    //For Edit delete Marker
    fun showMarkerMenu(){
        _showMarkerMenuEvent.value = Event(true)
    }

    fun editMarker(){
        _editMarkerTrigger.value = true
        //navigate(MapboxFragmentDirections.actionMapboxFragmentToEditLocationFragment())
        //Log.d("test_","editPin")
    }

    fun deleteMarker(){
        _confirmDeleteMarkerDialog.value = true
    }

    fun sendDeleteMarker(){
        val id = _idLocationLabel.value
        //call api
        deleteMarkerLocationQuery.execute(object : DisposableCompletableObserver(){
            override fun onComplete() {
                _refreshMapLocations.value = true
                Log.d("test_deleteMarkerLocationQuery","delete complete")
            }

            override fun onError(e: Throwable) {
                Log.d("deleteMarkerLocationQuery",e.toString())
            }
        }, params = id)
    }

    fun sendDeleteEventMarker(){
        val id = _idLocationLabel.value
        //call api
        deleteEventLocationQuery.execute(object : DisposableCompletableObserver(){
            override fun onComplete() {
                _refreshEventLocations.value = true
                Log.d("test_deleteMarkerLocationQuery","delete complete")
            }

            override fun onError(e: Throwable) {
                Log.d("deleteMarkerLocationQuery",e.toString())
            }
        }, params = id)
    }



    //For page navigate
    fun goToCreateMapBox(){
        navigate(MapboxFragmentDirections.actionMapboxFragment2ToCreateMapboxLocationFragment2(userLocation.value))
    }

    fun gotoCreatePolygonEvent() {
        navigate(MapboxFragmentDirections.actionMapboxFragmentToCreatePolygonEventFragment(userLocation.value))

    }

    fun gotoCreateCircleEvent() {
        navigate(MapboxFragmentDirections.actionMapboxFragmentToCreateCircleEventFragment(userLocation.value))
    }

    fun goBackClicked() {
        navigateBack()
    }

    fun goToEditMarker(){
        navigate(MapboxFragmentDirections.actionMapboxFragmentToEditLocationFragment(
            _idLocationLabel.value,
            _nameLocationLabel.value,
            _mapInformationResponse.value?.mapPoints?.firstOrNull{ _idLocationLabel.value == it.id.toString() }?.type,
            _descriptionLocationLabel.value,
            _imageLink.value?.toTypedArray()
        ))
    }

    fun goToEditEvent(){
        navigate(MapboxFragmentDirections.actionMapboxFragmentToEditEventFragment(
            _idLocationLabel.value,
            _nameLocationLabel.value,
            _descriptionLocationLabel.value,
            _imageLink.value?.toTypedArray(),
            _eventBindStart.value,
            _eventBindEnd.value
        ))
    }

}