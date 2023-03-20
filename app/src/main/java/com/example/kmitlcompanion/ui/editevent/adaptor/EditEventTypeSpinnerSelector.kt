package com.example.kmitlcompanion.ui.editevent.adaptor

import android.view.View
import android.widget.AdapterView
import com.example.kmitlcompanion.presentation.viewmodel.CreateEventViewModel
import com.example.kmitlcompanion.presentation.viewmodel.CreateLocationViewModel
import com.example.kmitlcompanion.presentation.viewmodel.EditEventViewModel
import com.example.kmitlcompanion.presentation.viewmodel.EditLocationViewModel
import com.example.kmitlcompanion.ui.createevent.utils.EventTypeUtils
import javax.inject.Inject

class EditEventTypeSpinnerSelector @Inject constructor(

): AdapterView.OnItemSelectedListener {

    lateinit var viewModel: EditEventViewModel
    @Inject lateinit var eventTypeUtils: EventTypeUtils
    fun setup(viewModel: EditEventViewModel) {
        this.viewModel = viewModel

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val value = p0?.getItemAtPosition(p2).toString()
        viewModel.updateEventType(value)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}