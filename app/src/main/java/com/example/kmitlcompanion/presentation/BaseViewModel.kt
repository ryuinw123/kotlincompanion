package com.example.kmitlcompanion.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.example.kmitlcompanion.presentation.navigation.NavigationCommand
import com.example.kmitlcompanion.presentation.eventobserver.Event

abstract class BaseViewModel : ViewModel() {

    private val _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation: LiveData<Event<NavigationCommand>> get() = _navigation

    fun navigate(navDirections: NavDirections){
        _navigation.value = Event(NavigationCommand.ToDirection(navDirections))
    }

    fun navigateBack(){
        _navigation.value = Event(NavigationCommand.Back)
    }

}