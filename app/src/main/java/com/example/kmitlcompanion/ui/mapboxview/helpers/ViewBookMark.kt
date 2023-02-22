package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.util.Log
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import javax.inject.Inject

class ViewBookMark @Inject constructor() {

    private lateinit var viewModel: MapboxViewModel

    fun setup(viewModel: MapboxViewModel){
        this.viewModel = viewModel
        Log.d("test_updatebookmark","update")
        viewModel.getAllBookMarker() // for marker bookmark
        viewModel.getAllEventBookMarker() //for event bookmark
    }

    fun onBookmarkClickHelper(){
        var eventState = viewModel.eventState.value
        if (eventState == true){
            viewModel.eventBookMarkUpdate()
        }else if (eventState == false){
            viewModel.bookMarkUpdate()
        }

    }

    fun updateBookmark(id : Int?, bookmarkStatus : Boolean?, eventState : Boolean?){
        var bookmakerList = viewModel.allMarkerBookmarked.value
        var bookmakerEventList = viewModel.allEventBookmarkedIdList.value
        if (eventState == true){
            bookmarkStatus?.let {
                if (bookmarkStatus){
                    bookmakerEventList?.add(id ?:0)
                }else{
                    bookmakerEventList?.remove(id)
                }
                Log.d("test_event_update_bookmark",bookmakerEventList.toString())
            }
        }else if (eventState == false){
            bookmarkStatus?.let {
                if (bookmarkStatus){
                    bookmakerList?.add(id ?:0)
                }else{
                    bookmakerList?.remove(id)
                }
            }
        }

    }


}