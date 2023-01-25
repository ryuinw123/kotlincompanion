package com.example.kmitlcompanion.ui.createlocation.helper

import android.content.Intent
import com.example.kmitlcompanion.presentation.viewmodel.CreateLocationViewModel
import javax.inject.Inject

class UploadHelper @Inject constructor(

) {
    private lateinit var viewModel: CreateLocationViewModel

    fun setup(viewModel: CreateLocationViewModel) {
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