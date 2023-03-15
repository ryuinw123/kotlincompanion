package com.example.kmitlcompanion.ui.report.helper

import android.content.Context
import android.widget.Spinner
import android.widget.TextView
import com.example.kmitlcompanion.presentation.viewmodel.ReportViewModel
import com.google.android.material.textfield.TextInputEditText
import javax.inject.Inject

class ViewReport @Inject constructor() {

    private lateinit var viewModel: ReportViewModel
    private lateinit var context: Context

    fun setup(viewModel: ReportViewModel,context: Context,eventState: Boolean,
              id : Long,
              name : String,
              reportHeader : TextView,
              nameReport : TextInputEditText
              ){

        this.context = context
        this.viewModel = viewModel

        reportHeader.text = "รายงาน" + if (eventState) "อีเวนต์" else "สถานที่"
        nameReport.setText(name)


        viewModel.setId(id)
        viewModel.setName(name)
        viewModel.setEventState(eventState)

    }


}