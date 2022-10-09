package com.example.kmitlcompanion.presentation

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {

    fun goBackClicked() {
        navigateBack()
    }

}