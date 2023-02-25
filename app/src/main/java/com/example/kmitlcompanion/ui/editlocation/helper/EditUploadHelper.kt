package com.example.kmitlcompanion.ui.editlocation.helper

import android.content.Intent
import com.example.kmitlcompanion.presentation.viewmodel.CreateEventViewModel
import com.example.kmitlcompanion.presentation.viewmodel.CreateLocationViewModel
import com.example.kmitlcompanion.presentation.viewmodel.EditLocationViewModel
import javax.inject.Inject

class EditUploadHelper @Inject constructor(

) {
    private lateinit var viewModel: EditLocationViewModel

    fun setup(viewModel: EditLocationViewModel) {
        this.viewModel = viewModel
    }

    fun uploadLocation() {
        viewModel.goToMapbox()
    }

}