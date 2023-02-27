package com.example.kmitlcompanion.ui.settingsedit.adaptor

import android.view.View
import android.widget.AdapterView
import com.example.kmitlcompanion.presentation.viewmodel.SettingsEditViewModel
import javax.inject.Inject

class SettingsEditSpinnerSelector @Inject constructor(

): AdapterView.OnItemSelectedListener {

    lateinit var viewModel: SettingsEditViewModel

    fun setup(viewModel: SettingsEditViewModel) {
        this.viewModel = viewModel

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val value = p0?.getItemAtPosition(p2).toString()
        viewModel.updateSpinnerTrigger(value)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}