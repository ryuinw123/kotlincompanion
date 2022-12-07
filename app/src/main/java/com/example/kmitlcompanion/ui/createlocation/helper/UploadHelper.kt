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

    fun uploadLocation() {
        viewModel.createLocation()
    }

}