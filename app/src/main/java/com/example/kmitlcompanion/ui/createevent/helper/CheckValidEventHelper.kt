package com.example.kmitlcompanion.ui.createevent.helper

import android.content.Context
import com.example.kmitlcompanion.presentation.viewmodel.CreateEventViewModel
import com.example.kmitlcompanion.presentation.viewmodel.CreateLocationViewModel
import javax.inject.Inject

class CheckValidEventHelper @Inject constructor() {

    private lateinit var viewModel:CreateEventViewModel
    private lateinit var context: Context

    fun setup(viewModel: CreateEventViewModel,context: Context){
        this.viewModel = viewModel
        this.context = context

        viewModel.getValidValue()

    }

}