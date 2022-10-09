package com.example.kmitlcompanion.presentation

import com.example.kmitlcompanion.ui.HomeFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {

    fun gotoMap(){
        navigate(HomeFragmentDirections.actionHomeFragmentToMapboxFragment2())
    }


    fun goBackClicked() {
        navigateBack()
    }



}