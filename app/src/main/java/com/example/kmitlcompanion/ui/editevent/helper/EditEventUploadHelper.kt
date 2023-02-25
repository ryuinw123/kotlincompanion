package com.example.kmitlcompanion.ui.editevent.helper

import com.example.kmitlcompanion.presentation.viewmodel.CreateEventViewModel
import com.example.kmitlcompanion.presentation.viewmodel.CreateLocationViewModel
import com.example.kmitlcompanion.presentation.viewmodel.EditEventViewModel
import javax.inject.Inject

class EditEventUploadHelper @Inject constructor(

) {
    private lateinit var viewModel: EditEventViewModel

    fun setup(viewModel: EditEventViewModel) {
        this.viewModel = viewModel
    }

    fun uploadLocation(public : Boolean) {
        viewModel.goToMapbox()
    }

}