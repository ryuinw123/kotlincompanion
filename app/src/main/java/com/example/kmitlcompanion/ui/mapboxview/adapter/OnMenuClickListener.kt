package com.example.kmitlcompanion.ui.mapboxview.adapter

import android.view.View
import com.example.kmitlcompanion.domain.model.Comment
import com.google.android.material.bottomsheet.BottomSheetDialog

interface OnMenuClickListener {
    fun onMenuClick(comment: Comment,bottomSheetDialog: BottomSheetDialog)
    fun onEditClick(comment: Comment,position : Int,bottomSheetDialog: BottomSheetDialog)
    fun onDeleteClick(comment: Comment,position : Int,bottomSheetDialog: BottomSheetDialog)
}