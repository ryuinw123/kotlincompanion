package com.example.kmitlcompanion.ui.eventpage.helper

import android.content.Context
import com.example.kmitlcompanion.presentation.viewmodel.NotiLogPageViewModel
import javax.inject.Inject

class ViewNotiLog @Inject constructor() {

    private lateinit var viewModel: NotiLogPageViewModel
    private lateinit var context: Context


    fun setup(viewModel: NotiLogPageViewModel,context: Context){
        this.viewModel = viewModel
        this.context = context

        viewModel.downloadNotiLog()
    }

}