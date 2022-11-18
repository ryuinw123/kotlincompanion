package com.example.kmitlcompanion.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kmitlcompanion.domain.model.Comment
import com.example.kmitlcompanion.domain.model.MapInformation
import com.example.kmitlcompanion.domain.usecases.GetMapLocations
import com.example.kmitlcompanion.ui.mapboxview.MapboxFragmentDirections
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.observers.DisposableObserver
import javax.inject.Inject

@HiltViewModel
class MapboxViewModel @Inject constructor(
    private val getMapLocations: GetMapLocations
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

    private val _positionFlyer = MutableLiveData<Point>()
    val positionFlyer: LiveData<Point> = _positionFlyer

    private val _descriptionLocationLabel = MutableLiveData<String?>()
    val descriptionLocationLabel : LiveData<String?> = _descriptionLocationLabel






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

    fun updateBottomSheetState(state: Int) {
        _bottomSheetState.value = state
    }

    fun updateCurrentLocationGps(point: Point){
        val lat = point.latitude()
        val long = point.longitude()
        _currentLocationGps.value = "Lat: $lat, Long: $long"
    }
    fun updateIdLocationLabel(id : String){
        _idLocationLabel.value = "id = $id"
    }
    fun updateNameLocationLabel(name : String){
        _nameLocationLabel.value = "name = $name"
    }
    fun updateDescriptionLocationLabel(description : String) {
        _descriptionLocationLabel.value = "description = $description"
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




    //test
    private val nestedComment = mutableListOf(
        Comment(5, "15/05/2021 at 12:25", "Anonymous5", "NestedComment1", 2, parentId = 1),
        Comment(6,"15/05/2021 at 12:26", "Anonymous6", "NestedComment2", 2, parentId = 1)
    )

    private val _commentList = MutableLiveData(mutableListOf(
        Comment(1, "15/05/2021 at 12:20", "Anonymous1", "Comment1", replies = nestedComment),
        Comment(2, "15/05/2021 at 12:22", "Anonymous2", "Comment2"),
        Comment(3, "15/05/2021 at 12:23", "Anonymous3", "Comment3"),
        Comment(4, "15/05/2021 at 12:24", "Anonymous4", "Comment4"),
    ))
    val commentList: LiveData<MutableList<Comment>> = _commentList

    fun addComment(shortComment: Comment) {
        val newList = _commentList.value ?: mutableListOf()
        if(shortComment.parentId != null) {
            newList.find { it.id == shortComment.parentId }?.replies?.add(shortComment)
        } else {
            newList.add(shortComment)
        }
        _commentList.value = newList
    }

}