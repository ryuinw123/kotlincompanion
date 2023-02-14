package com.example.kmitlcompanion.ui.createevent.helper

import com.example.kmitlcompanion.presentation.viewmodel.CreateEventViewModel
import com.example.kmitlcompanion.presentation.viewmodel.CreateLocationViewModel
import javax.inject.Inject

class UploadHelper @Inject constructor(

) {
    private lateinit var viewModel: CreateEventViewModel

    fun setup(viewModel: CreateEventViewModel) {
        this.viewModel = viewModel
    }

    fun uploadLocation(public : Boolean) {
        when (public) {
            true -> viewModel.publicLocation()
            false -> viewModel.privateLocation()
        }
        viewModel.goToMapbox()
    }

}