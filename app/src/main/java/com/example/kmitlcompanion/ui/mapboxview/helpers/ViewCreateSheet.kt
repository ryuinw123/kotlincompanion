package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.CreateEventBinding
import com.example.kmitlcompanion.databinding.CreateMenuBinding
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.lang.ref.WeakReference
import javax.inject.Inject

class ViewCreateSheet @Inject constructor(
) {
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var createMenuBinding: CreateMenuBinding
    private lateinit var createEventBinding: CreateEventBinding

    fun setup (context: Context , viewModel: MapboxViewModel) {
        bottomSheetDialog = BottomSheetDialog(context)
        createMenuBinding = CreateMenuBinding.inflate(LayoutInflater.from(context))
        createMenuBinding.let {
            createMenuBinding.viewModel = viewModel
        }

        createEventBinding = CreateEventBinding.inflate(LayoutInflater.from(context))
        createEventBinding.let {
            createEventBinding.viewModel = viewModel
        }

    }

    fun dropBottomSheet() {
        bottomSheetDialog.dismiss()
    }

    fun openCreateSheetDialog() {
        bottomSheetDialog.setContentView(createMenuBinding.root)

        bottomSheetDialog.show()
    }

    fun openCreateEventDialog() {
        bottomSheetDialog.setContentView(createEventBinding.root)
        bottomSheetDialog.show()
    }








}