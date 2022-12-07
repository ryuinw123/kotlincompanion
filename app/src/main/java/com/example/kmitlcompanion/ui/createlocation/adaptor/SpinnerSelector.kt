package com.example.kmitlcompanion.ui.createlocation.adaptor

import android.view.View
import android.widget.AdapterView
import com.example.kmitlcompanion.presentation.viewmodel.CreateLocationViewModel
import javax.inject.Inject

class SpinnerSelector @Inject constructor(

): AdapterView.OnItemSelectedListener {

    lateinit var viewModel: CreateLocationViewModel

    fun setup(viewModel: CreateLocationViewModel) {
        this.viewModel = viewModel

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val value = p0?.getItemAtPosition(p2).toString()
        viewModel.updateTypeSpinner(value)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}