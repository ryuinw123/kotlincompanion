package com.example.kmitlcompanion.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.example.kmitlcompanion.presentation.navigation.NavigationCommand
import com.example.kmitlcompanion.presentation.eventobserver.Event

abstract class BaseViewModel : ViewModel() {

    private val _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation: LiveData<Event<NavigationCommand>> get() = _navigation

    private val _navigationOption = MutableLiveData<Event<Pair<NavigationCommand,NavOptions>>>()
    val navigationOption: LiveData<Event<Pair<NavigationCommand,NavOptions>>> get() = _navigationOption

    fun navigate(navDirections: NavDirections){
        _navigation.value = Event(NavigationCommand.ToDirection(navDirections))
    }

    fun navigate(navDirections: NavDirections,navOptions: NavOptions){
        _navigationOption.value = Event(Pair(NavigationCommand.ToDirection(navDirections),navOptions))

    }

    fun navigateBack(){
        _navigation.value = Event(NavigationCommand.Back)
    }

}