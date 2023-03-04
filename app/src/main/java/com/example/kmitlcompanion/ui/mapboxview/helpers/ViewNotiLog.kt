package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.content.Context
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import javax.inject.Inject

class ViewNotiLog @Inject constructor() {

    private lateinit var viewModel: MapboxViewModel
    private lateinit var context: Context

    fun setup(viewModel: MapboxViewModel,context: Context){
        this.viewModel = viewModel
        this.context = context
    }




}