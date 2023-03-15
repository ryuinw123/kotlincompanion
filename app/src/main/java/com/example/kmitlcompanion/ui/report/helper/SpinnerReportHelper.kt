package com.example.kmitlcompanion.ui.report.helper

import android.content.Context
import android.content.res.Resources
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.Spinner
import androidx.appcompat.content.res.AppCompatResources
import com.example.kmitlcompanion.presentation.viewmodel.CreateLocationViewModel
import com.example.kmitlcompanion.presentation.viewmodel.ReportViewModel
import com.example.kmitlcompanion.ui.createlocation.adaptor.SpinnerSelector
import com.example.kmitlcompanion.ui.report.adaptor.SpinnerReportSelector
import javax.inject.Inject

class SpinnerReportHelper @Inject constructor(
    private val spinnerSelector: SpinnerReportSelector
)  {

    private lateinit var spinner : Spinner
    private lateinit var viewModel: ReportViewModel

    fun setup(spinner: Spinner , itemList : ArrayList<String>,context: Context , viewModel: ReportViewModel) {
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