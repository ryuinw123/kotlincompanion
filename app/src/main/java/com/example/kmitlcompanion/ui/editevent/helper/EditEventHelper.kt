package com.example.kmitlcompanion.ui.editevent.helper

import com.example.kmitlcompanion.presentation.viewmodel.EditEventViewModel
import com.example.kmitlcompanion.presentation.viewmodel.EditLocationViewModel
import com.example.kmitlcompanion.ui.editevent.adaptor.EditEventTypeSpinnerSelector
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class EditEventHelper @Inject constructor(
    val image: EditEventImageHelper,
    val upload : EditEventUploadHelper,
    val datetime : EditEventDateTimePickHelper,
    val spinner : EditEventTypeSpinnerHelper,
) {

    private lateinit var viewModel: EditEventViewModel

    fun setup(viewModel : EditEventViewModel){
        this.viewModel = viewModel
        viewModel.editEventResponse.value = true
    }

    fun updateData(id : String?,name : String?,des : String?,startTime : String?,endTime : String?,eventType : String?
                    ,eventUrl : String?){
        viewModel.updateId(id)
        viewModel.updateNameInput(name)
        viewModel.updateDetailInput(des)
        viewModel.setStartDateTimePick(startTime ?:"")
        viewModel.setEndDateTimePick(endTime ?:"")
        viewModel.updateEventType(eventType ?:"")
        viewModel.updateEventUrl(eventUrl ?:"")
    }

}