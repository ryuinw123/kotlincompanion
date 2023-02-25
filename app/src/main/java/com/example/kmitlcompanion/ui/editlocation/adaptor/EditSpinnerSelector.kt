package com.example.kmitlcompanion.ui.editlocation.adaptor

import android.view.View
import android.widget.AdapterView
import com.example.kmitlcompanion.presentation.viewmodel.CreateLocationViewModel
import com.example.kmitlcompanion.presentation.viewmodel.EditLocationViewModel
import javax.inject.Inject

class EditSpinnerSelector @Inject constructor(

): AdapterView.OnItemSelectedListener {

    lateinit var viewModel: EditLocationViewModel

    fun setup(viewModel: EditLocationViewModel) {
        this.viewModel = viewModel

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val value = p0?.getItemAtPosition(p2).toString()
        viewModel.updateTypeSpinner(value)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}