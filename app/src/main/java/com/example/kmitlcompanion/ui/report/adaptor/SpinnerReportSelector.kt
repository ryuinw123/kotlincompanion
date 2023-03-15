package com.example.kmitlcompanion.ui.report.adaptor

import android.graphics.Paint
import android.view.View
import android.widget.AdapterView
import com.example.kmitlcompanion.presentation.viewmodel.CreateLocationViewModel
import com.example.kmitlcompanion.presentation.viewmodel.EditLocationViewModel
import com.example.kmitlcompanion.presentation.viewmodel.ReportViewModel
import javax.inject.Inject

class SpinnerReportSelector @Inject constructor(

): AdapterView.OnItemSelectedListener {

    lateinit var viewModel: ReportViewModel

    fun setup(viewModel: ReportViewModel) {
        this.viewModel = viewModel

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val value = p0?.getItemAtPosition(p2).toString()
        viewModel.updateSpinner(value)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}