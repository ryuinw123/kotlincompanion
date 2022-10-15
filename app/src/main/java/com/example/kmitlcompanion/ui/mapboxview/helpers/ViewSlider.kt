package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.lang.ref.WeakReference
import javax.inject.Inject

internal class ViewSlider @Inject constructor() {
    private var weaksheetBehavior: WeakReference<BottomSheetBehavior<ViewGroup>?>? = null

    fun setup(bottomSheet: ViewGroup){
        weaksheetBehavior = WeakReference(BottomSheetBehavior.from(bottomSheet))
        setupBottomSheetListeners()
    }

    private fun setupBottomSheetListeners() {
        sheetBehavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                TODO("Not yet implemented")
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                TODO("Not yet implemented")
            }
        })
    }

    fun setState(state: Int) {
        sheetBehavior?.state = state
    }

    class AutoCloseBottomSheetBehavior<V : View>(context: Context, attrs: AttributeSet) :
        BottomSheetBehavior<V>(context, attrs) {

        override fun onInterceptTouchEvent(
            parent: CoordinatorLayout,
            child: V,
            event: MotionEvent
        ): Boolean {
            if (event.action == MotionEvent.ACTION_DOWN && state == STATE_EXPANDED) {

                val outRect = Rect()
                child.getGlobalVisibleRect(outRect)

                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    state = STATE_COLLAPSED
                }
            }
            return super.onInterceptTouchEvent(parent, child, event)
        }
    }

    private val sheetBehavior
        get() = weaksheetBehavior?.get()
}