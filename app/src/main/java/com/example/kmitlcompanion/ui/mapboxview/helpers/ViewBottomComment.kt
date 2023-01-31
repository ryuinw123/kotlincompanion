package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.lang.ref.WeakReference
import javax.inject.Inject
import com.google.android.material.bottomsheet.BottomSheetDialog

class ViewBottomComment @Inject constructor(

){
    private var weaksheetBehavior: WeakReference<BottomSheetBehavior<ViewGroup>?>? = null

    private lateinit var bottomSheetDialog : BottomSheetDialog

    fun setup(bottomSheet: ConstraintLayout,viewModel: MapboxViewModel){

        //bottomSheetDialog.setContentView()
        //weaksheetBehavior = WeakReference(BottomSheetBehavior.from(bottomSheet))
        //sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private val sheetBehavior
        get() = weaksheetBehavior?.get()
}