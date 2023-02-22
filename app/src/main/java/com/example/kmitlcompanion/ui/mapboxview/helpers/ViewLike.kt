package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.content.Context
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import javax.inject.Inject

class ViewLike @Inject constructor() {

    private lateinit var viewModel: MapboxViewModel
    private lateinit var context: Context

    fun setup(viewModel: MapboxViewModel,context: Context){
        this.viewModel = viewModel
        this.context = context
    }

    fun addLikeLocationQueryHelper(id : String?){
        var eventState = viewModel.eventState.value

        if (eventState == true){
            viewModel.changeLikeLocationQueryEvent(id,true,1 + viewModel.likeCoutingUpdate.value!!) // event
        }else if (eventState == false){
            viewModel.addLikeLocationQuery(id)
        }
    }


    fun removeLikeLocationQueryHelper(id : String?){
        var eventState = viewModel.eventState.value

        if (eventState == true){
            viewModel.changeLikeLocationQueryEvent(id,false,viewModel.likeCoutingUpdate.value!!-1) // event
        }else if (eventState == false){
            viewModel.removeLikeLocationQuery(id)
        }
    }

}