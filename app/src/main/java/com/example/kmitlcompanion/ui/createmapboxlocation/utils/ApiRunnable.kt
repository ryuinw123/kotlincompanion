package com.example.kmitlcompanion.ui.createmapboxlocation.utils

import com.example.kmitlcompanion.presentation.viewmodel.CreateMapboxLocationViewModel
import javax.inject.Inject

class ApiRunnable @Inject constructor(

): Runnable
{
    private lateinit var viewModel : CreateMapboxLocationViewModel

    fun setup(viewModel: CreateMapboxLocationViewModel){
        this.viewModel = viewModel
    }

    override fun run() {
        this.viewModel.getLocationQuery(viewModel.currentMapLocation.value)
    }
}