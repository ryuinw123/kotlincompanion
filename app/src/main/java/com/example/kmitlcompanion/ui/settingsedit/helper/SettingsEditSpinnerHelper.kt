package com.example.kmitlcompanion.ui.createlocation.helper

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.Spinner
import androidx.appcompat.content.res.AppCompatResources
import com.example.kmitlcompanion.presentation.viewmodel.CreateLocationViewModel
import com.example.kmitlcompanion.presentation.viewmodel.EditLocationViewModel
import com.example.kmitlcompanion.presentation.viewmodel.SettingsEditViewModel
import com.example.kmitlcompanion.ui.createlocation.adaptor.SpinnerSelector
import com.example.kmitlcompanion.ui.editlocation.adaptor.EditSpinnerSelector
import com.example.kmitlcompanion.ui.identitylogin.DataFacultyDepart
import com.example.kmitlcompanion.ui.settingsedit.SettingsEditFragmentArgs
import com.example.kmitlcompanion.ui.settingsedit.adaptor.SettingsEditSpinnerSelector
import javax.inject.Inject

class SettingsEditSpinnerHelper @Inject constructor(
    private val spinnerSelector: SettingsEditSpinnerSelector
)  {

    private lateinit var spinner : Spinner
    private lateinit var viewModel: SettingsEditViewModel

    private val dataFacultyDepart = DataFacultyDepart()

    fun setup(spinner: Spinner, navArgument: SettingsEditFragmentArgs, context: Context, viewModel: SettingsEditViewModel) {
        this.spinner = spinner
        this.viewModel = viewModel

        var itemList = mutableListOf<String>()

        when(navArgument.state){
            2 -> itemList = dataFacultyDepart.getFaculty()
            3 -> itemList = dataFacultyDepart.getDepart(navArgument.faculty ?:"")
            4 -> itemList = mutableListOf(1,2,3,4,5,6,7,8).map { it.toString() }.toMutableList()
        }


        val adapter = ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item,itemList)
        spinner.adapter = adapter
        spinnerSelector.setup(viewModel)
        spinner.onItemSelectedListener = spinnerSelector
        limitDropDownHeight(spinner,context.resources)
    }
    private fun limitDropDownHeight(spinner : Spinner, resources: Resources) {
        val popup = Spinner::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val popupWindow = popup.get(spinner) as ListPopupWindow
        popupWindow.height = 0
    }

    fun setSpinner(text: String){
        for (i in 0 until spinner.adapter.count) {
            if (spinner.adapter.getItem(i).toString() == text) {
                spinner.setSelection(i)
            }
        }
    }


}