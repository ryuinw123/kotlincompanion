package com.example.kmitlcompanion.ui.createevent.helper

import android.content.Context
import android.content.res.Resources
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.Spinner
import androidx.appcompat.content.res.AppCompatResources
import com.example.kmitlcompanion.presentation.viewmodel.CreateEventViewModel
import com.example.kmitlcompanion.presentation.viewmodel.CreateLocationViewModel
import com.example.kmitlcompanion.ui.createevent.adaptor.EventTypeSpinnerSelector
import com.example.kmitlcompanion.ui.createlocation.adaptor.SpinnerSelector
import javax.inject.Inject

class EventTypeSpinnerHelper @Inject constructor(
    private val spinnerSelector: EventTypeSpinnerSelector
)  {

    private lateinit var spinner : Spinner
    private lateinit var viewModel: CreateEventViewModel

    fun setup(spinner: Spinner , itemList : ArrayList<String>,context: Context , viewModel: CreateEventViewModel) {
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


}