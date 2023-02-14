package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.util.Log
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import javax.inject.Inject

class ViewBookMark @Inject constructor() {

    private lateinit var viewModel: MapboxViewModel

    fun setup(viewModel: MapboxViewModel){
        this.viewModel = viewModel
        Log.d("test_updatebookmark","update")
        viewModel.getAllBookMarker()
    }

    fun updateBookmark(markerId : Int?,status : Boolean?){
        var bookmakerList = viewModel.allMarkerBookmarked.value
        status?.let {
            if (status){
                bookmakerList?.add(markerId ?:0)
            }else{
                bookmakerList?.remove(markerId)
            }
        }
    }


}