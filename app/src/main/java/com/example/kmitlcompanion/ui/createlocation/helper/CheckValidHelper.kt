package com.example.kmitlcompanion.ui.createlocation.helper

import android.content.Context
import com.example.kmitlcompanion.presentation.viewmodel.CreateLocationViewModel
import javax.inject.Inject

class CheckValidHelper @Inject constructor() {

    private lateinit var viewModel:CreateLocationViewModel
    private lateinit var context: Context

    fun setup(viewModel: CreateLocationViewModel,context: Context){
        this.viewModel = viewModel
        this.context = context

        viewModel.getValidValue()

    }

}