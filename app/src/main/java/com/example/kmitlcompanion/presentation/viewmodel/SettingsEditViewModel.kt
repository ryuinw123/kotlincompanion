package com.example.kmitlcompanion.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavOptions
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentSettingsEditBinding
import com.example.kmitlcompanion.domain.CompletableUseCase
import com.example.kmitlcompanion.domain.model.UserEditData
import com.example.kmitlcompanion.domain.model.UserSettingsData
import com.example.kmitlcompanion.domain.usecases.SettingsEditUpdateUserData
import com.example.kmitlcompanion.presentation.BaseViewModel
import com.example.kmitlcompanion.presentation.eventobserver.Event
import com.example.kmitlcompanion.ui.settingsedit.SettingsEditFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.observers.DisposableObserver
import javax.inject.Inject

@HiltViewModel
class SettingsEditViewModel @Inject constructor(
    private val settingsEditUpdateUserData: SettingsEditUpdateUserData,
) : BaseViewModel()  {


    private val _editState = MutableLiveData<Int>()
    val editState : MutableLiveData<Int> = _editState

    private val _editUserData = MutableLiveData<UserEditData>(
        UserEditData(
            username = "",
            faculty = "",
            department = "",
            year = ""
        )
    )
    val editUserData : LiveData<UserEditData>  = _editUserData

    private val _updateSpinner = MutableLiveData<String>()
    val updateSpinner : MutableLiveData<String> = _updateSpinner

    private val _spinnerTrigger = MutableLiveData<String>()
    val spinnerTrigger : MutableLiveData<String> = _spinnerTrigger

    private val _blockSubmit = MutableLiveData<Event<Boolean>>()
    val blockSubmit :LiveData<Event<Boolean>> = _blockSubmit

    val headerText = mutableListOf<String>("แก้ไขชื่อผู้ใช้","","แก้ไขคณะ","แก้ไขสาขา","แก้ไขชั้นปี")
    val headText = mutableListOf<String>("ชื่อผู้ใช้","","คณะ","สาขา","ชั้นปี")

    fun setState(state : Int){
        _editState.value =  state
    }

    fun changeSubmitBySpin(state : Boolean){
        _blockSubmit.value = Event(state)
    }

    fun updateSpinnerTrigger(data : String){
        _spinnerTrigger.value = data
    }

    fun updateUserFull(text : String){
        val newUserData = UserEditData(text,
            _editUserData.value?.faculty,_editUserData.value?.department,_editUserData.value?.year)
        _editUserData.value = newUserData
    }

    fun updateUserNameInput(text : String){
        val newUserData = UserEditData(text +" "+ _editUserData.value?.username?.split(" ")?.get(1),
            _editUserData.value?.faculty,_editUserData.value?.department,_editUserData.value?.year)
        _editUserData.value = newUserData
    }

    fun updateUserLastNameInput(text : String){
        val newUserData = UserEditData(_editUserData.value?.username?.split(" ")?.get(0) +" "+ text,
            _editUserData.value?.faculty,_editUserData.value?.department,_editUserData.value?.year)
        _editUserData.value = newUserData
    }

    fun updateFaculty(text : String){
        val newUserData = UserEditData(_editUserData.value?.username,
            text,"ไม่ได้เลือก",_editUserData.value?.year)
        _editUserData.value = newUserData
    }

    fun updateDepartment(text : String){
        val newUserData = UserEditData(_editUserData.value?.username,
            _editUserData.value?.faculty,text,_editUserData.value?.year)
        _editUserData.value = newUserData
    }

    fun updateYear(text : String){
        val newUserData = UserEditData(_editUserData.value?.username,
            _editUserData.value?.faculty,_editUserData.value?.department,text)
        _editUserData.value = newUserData
    }

    fun setSpinner(text : String){
        _updateSpinner.value = text
    }

    fun saveEdit(){
        //navigate(SettingsEditFragmentDirections.actionSettingsEditFragmentToSettingsFragment(),navOptions)
        settingsEditUpdateUserData.execute(object : DisposableCompletableObserver() {
            override fun onComplete() {
                goBack()
            }

            override fun onError(e: Throwable) {
                Log.d("test_settings",e.toString())
            }
        }, params = _editUserData.value)
    }


    private fun goBack(){
        navigateBack()
    }

}