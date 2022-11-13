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

    fun setup(bottomNavigationView : CoordinatorLayout) {
        this.weakBottomNavigationView = WeakReference(bottomNavigationView)
        number = "2"
        Log.d("Debug","AlreadyInitBottomBar")
    }

    val bottomMap
        get() = weakBottomNavigationView?.get()
}