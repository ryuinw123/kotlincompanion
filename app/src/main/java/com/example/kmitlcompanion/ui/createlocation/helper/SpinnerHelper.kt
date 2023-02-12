package com.example.kmitlcompanion.ui.createlocation.helper

import android.content.Context
import android.content.res.Resources
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.Spinner
import androidx.appcompat.content.res.AppCompatResources
import com.example.kmitlcompanion.presentation.viewmodel.CreateLocationViewModel
import com.example.kmitlcompanion.ui.createlocation.adaptor.SpinnerSelector
import javax.inject.Inject

class SpinnerHelper @Inject constructor(
    private val spinnerSelector: SpinnerSelector
)  {

    private lateinit var spinner : Spinner
    private lateinit var viewModel: CreateLocationViewModel

    fun setup(spinner: Spinner , itemList : ArrayList<String>,context: Context , viewModel: CreateLocationViewModel) {
        this.spinner = spinner
        this.viewModel = viewModel
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

    fun turnoffSpinner() {
        this.viewModel.updateTypeSpinner("อีเวนท์")
        //เดียวลบ setselection
        this.spinner.setSelection(3)
        this.spinner.isEnabled = false
    }

}