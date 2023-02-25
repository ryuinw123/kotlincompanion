package com.example.kmitlcompanion.ui.editlocation.helper

import com.example.kmitlcompanion.presentation.viewmodel.EditLocationViewModel
import com.example.kmitlcompanion.ui.createlocation.helper.EditSpinnerHelper
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
@ActivityScoped
class EditLocationHelper @Inject constructor(
    val image: EditImageHelper,
    val spinner : EditSpinnerHelper,
    val upload : EditUploadHelper
){

    private lateinit var viewModel: EditLocationViewModel

    fun setup(viewModel : EditLocationViewModel){
        this.viewModel = viewModel
        viewModel.editLocationResponse.value = true
    }

    fun updateData(id : String?,name : String?,des : String?,type : String?){
        viewModel.updateId(id)
        viewModel.updateNameInput(name)
        viewModel.updateTypeSpinner(type ?:"")
        viewModel.updateDetailInput(des)
    }
}