package com.example.kmitlcompanion.ui.identitylogin.helper

import com.example.kmitlcompanion.presentation.IdentityloginViewModel
import com.example.kmitlcompanion.ui.identitylogin.utils.ValidationMock
import javax.inject.Inject

class ValidatationHelper @Inject constructor(
    private val validationMock: ValidationMock
) {
    private lateinit var viewModel: IdentityloginViewModel

    fun setup(viewModel: IdentityloginViewModel){
        this.viewModel = viewModel
    }

    fun validate(){
        if (validationMock.validate(viewModel.textHintInputName.value!!)) {
            viewModel.updateTextInputNameHelper("ห้ามมีตัวอักษรพิเศษ และห้ามเว้นว่าง")
        }
        viewModel.updateTextInputNameHelper("")
    }
    fun inputValidation() {
        if (validationMock.checkAllFieldValid(viewModel.textNameValue2.value!!,viewModel.textHintInputName.value!!)) {
            viewModel.saveData()
        }
    }


}
