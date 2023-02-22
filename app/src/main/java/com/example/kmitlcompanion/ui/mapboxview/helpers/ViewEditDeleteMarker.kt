package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import com.example.kmitlcompanion.databinding.MarkerMenuBinding
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.NonDisposableHandle.parent
import java.lang.ref.WeakReference
import javax.inject.Inject

class ViewEditDeleteMarker @Inject constructor(
) {
    private lateinit var viewModel: MapboxViewModel
    private lateinit var context: Context
    private lateinit var markerMenuBinding: MarkerMenuBinding
    private lateinit var weakEditDeleteButton: WeakReference<Button>
    private lateinit var bottomSheetDialog : BottomSheetDialog
    private lateinit var alertBuilder : AlertDialog.Builder


    fun setup(viewModel: MapboxViewModel,context: Context,editDeleteButton: Button){
        this.viewModel = viewModel
        this.context = context
        this.weakEditDeleteButton = WeakReference(editDeleteButton)
        this.markerMenuBinding = MarkerMenuBinding.inflate(LayoutInflater.from(context))


        this.bottomSheetDialog = BottomSheetDialog(context)
        this.markerMenuBinding.viewModel = viewModel

        this.alertBuilder = AlertDialog.Builder(context)

    }

    fun showMarkerMenu(){
        bottomSheetDialog.setContentView(markerMenuBinding.root)
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.show()
    }

    fun showConfirmDeleteDialog(){
        bottomSheetDialog.dismiss()
        Log.d("test_","delete pin ")
        if (viewModel.eventState.value == false) {
            alertBuilder.setTitle("ลบปักหมุดของคุณ")
            alertBuilder.setMessage("คุณต้องการลบปักหมุดของคุณอย่างถาวรหรือไม่ ?")
        }else if(viewModel.eventState.value == true) {
            alertBuilder.setTitle("ลบอีเวนต์ของคุณ")
            alertBuilder.setMessage("คุณต้องการลบอีเวนต์ของคุณอย่างถาวรหรือไม่ ?")
        }
        alertBuilder.setNegativeButton("ไม่",null)
        alertBuilder.setPositiveButton("ใช่", DialogInterface.OnClickListener { _, _ ->
            viewModel.updateBottomSheetState(BottomSheetBehavior.STATE_HIDDEN)
            if (viewModel.eventState.value == true){
                viewModel.sendDeleteEventMarker()
            }else if(viewModel.eventState.value == false){
                viewModel.sendDeleteMarker()
            }

        })
        alertBuilder.show()
    }

    private val editDeleteButton
        get() = weakEditDeleteButton?.get()


}