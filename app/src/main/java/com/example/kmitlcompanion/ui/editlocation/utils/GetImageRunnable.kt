package com.example.kmitlcompanion.ui.editlocation.utils

import com.example.kmitlcompanion.presentation.viewmodel.EditLocationViewModel
import javax.inject.Inject

class GetImageRunnable @Inject constructor(

): Runnable {

    private lateinit var viewModel: EditLocationViewModel

    fun setup(viewModel: EditLocationViewModel){
        this.viewModel = viewModel
    }

    override fun run() {
    }
}