package com.example.kmitlcompanion.ui.settingsedit.helper

import android.content.Context
import android.util.Log
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.kmitlcompanion.presentation.viewmodel.SettingsEditViewModel
import com.example.kmitlcompanion.ui.createlocation.helper.SettingsEditSpinnerHelper
import com.example.kmitlcompanion.ui.settingsedit.SettingsEditFragmentArgs
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class SettingsEditHelper @Inject constructor(
    val spinner : SettingsEditSpinnerHelper
) {

    private lateinit var viewModel: SettingsEditViewModel
    private lateinit var context: Context
    private lateinit var navArgument: SettingsEditFragmentArgs

    fun setup(viewModel: SettingsEditViewModel, context: Context,
              textHeader : TextView,
              textBar : ConstraintLayout,
              spinner : ConstraintLayout,
              textInput : TextInputEditText,
              textInput2 : TextInputEditText,
              spinHead : TextView,
              editSpinner : Spinner,
              navArgument: SettingsEditFragmentArgs,
              ){

        this.viewModel = viewModel
        this.context = context
        this.navArgument = navArgument

        viewModel.setState(navArgument.state)
        val state = navArgument.state
        textHeader.text = viewModel.headerText[state]
        textInput.setText(navArgument.username?.split(" ")?.get(0) ?:"")
        textInput2.setText(navArgument.username?.split(" ")?.get(1) ?:"")
        textInput.hint = viewModel.headText[state]
        spinHead.text = viewModel.headText[state]


        when (state){
            2 -> viewModel.setSpinner(navArgument.faculty ?:"")
            3 -> viewModel.setSpinner(navArgument.department ?:"")
            4 -> viewModel.setSpinner(navArgument.year ?:"")
        }

        //data
        viewModel.updateUserFull(navArgument.username ?:" ")
        viewModel.updateFaculty(navArgument.faculty ?:"")
        viewModel.updateDepartment(navArgument.department ?:"")
        viewModel.updateYear(navArgument.year ?:"")

    }




    fun updateSpinnerByState(text : String){
        Log.d("test_edit","update spinger")
        when(navArgument.state){
            2 -> viewModel.updateFaculty(text)
            3 -> viewModel.updateDepartment(text)
            4 -> viewModel.updateYear(text)
        }
    }

}