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
import com.example.kmitlcompanion.ui.createlocation.adaptor.SpinnerSelector
import com.example.kmitlcompanion.ui.editlocation.adaptor.EditSpinnerSelector
import javax.inject.Inject

class EditSpinnerHelper @Inject constructor(
    private val spinnerSelector: EditSpinnerSelector
)  {

    private lateinit var spinner : Spinner
    private lateinit var viewModel: EditLocationViewModel

    fun setup(spinner: Spinner , itemList : ArrayList<String>,context: Context , viewModel: EditLocationViewModel) {
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

    fun setSpinner(text: String){

        for (i in 0 until spinner.adapter.count) {
            if (spinner.adapter.getItem(i).toString().contains(text)) {
                spinner.setSelection(i)
            }
        }
    }


}