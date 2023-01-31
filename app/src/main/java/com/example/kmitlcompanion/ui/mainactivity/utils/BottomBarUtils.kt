package com.example.kmitlcompanion.ui.mainactivity.utils

import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

class BottomBarUtils @Inject constructor() {
    private lateinit var weakBottomNavigationView: WeakReference<CoordinatorLayout>
    var number = String()

    var applicationMode = 0
        set(value) {
            field = value
            bottomStateBehavior()
        }
    var sliderState = 0
        set(value) {
            field = value
            bottomStateBehavior()
        }

    fun bottomStateBehavior() {
        if (applicationMode == 1) {
            bottomMap?.visibility = View.INVISIBLE
        }
        else {
            if (sliderState == 5) {
                bottomMap?.visibility = View.VISIBLE
            }
            else {
                bottomMap?.visibility = View.INVISIBLE
            }
        }
    }

    fun setup(bottomNavigationView : CoordinatorLayout) {
        this.weakBottomNavigationView = WeakReference(bottomNavigationView)
        number = "2"
        Log.d("Debug","AlreadyInitBottomBar")
    }

    val bottomMap
        get() = weakBottomNavigationView?.get()
}