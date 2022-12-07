package com.example.kmitlcompanion.ui.mainactivity.helper

import android.view.View
import android.view.Window
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.kmitlcompanion.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.ref.WeakReference
import javax.inject.Inject

class  NavHelper  @Inject constructor(){
    private var weakNavHostFragment: WeakReference<FragmentContainerView>? = null
    private var weakBottomMap : WeakReference<BottomNavigationView>? = null





    fun setup(window: Window,navHostFragment:FragmentContainerView,root : ConstraintLayout,bottomNavigation : BottomNavigationView,bottomMap: BottomNavigationView){

        this.weakNavHostFragment = WeakReference(navHostFragment)
        this.weakBottomMap = WeakReference(bottomMap)
        this.appearanceLightNavigationBars(window)
        this.setDrawUIonTop(root)
        this.setWindowInsetsController(window)
        this.drawBottomNavBar(window,bottomNavigation)

    }

    private fun appearanceLightNavigationBars(window : Window){

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView)

        windowInsetsController?.isAppearanceLightNavigationBars = true
    }

    private fun setDrawUIonTop(root: ConstraintLayout){

        ViewCompat.setOnApplyWindowInsetsListener(
            root
        ) { view: View, windowInsets: WindowInsetsCompat ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.layoutParams = (view.layoutParams as FrameLayout.LayoutParams).apply {
                // draw on top of the bottom navigation bar
                bottomMargin = insets.bottom
            }

            WindowInsetsCompat.CONSUMED
        }
        /////////////////////////////////////////
    }

    private fun setWindowInsetsController(window: Window){
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsCompat = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsCompat.hide(WindowInsetsCompat.Type.statusBars())
        windowInsetsCompat.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    private fun drawBottomNavBar(window:Window,bottomNavigation: BottomNavigationView){
        val navController = navHostFragment?.getFragment<NavHostFragment>()?.navController
        NavigationUI.setupWithNavController(bottomNavigation, navController!!)

        NavigationUI.setupWithNavController(bottomMap!!, navController!!)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN



    }

    fun navigateToMap() {
        bottomMap?.selectedItemId = R.id.mapboxFragment
    }

    private val navHostFragment
        get() = weakNavHostFragment?.get()
    private val bottomMap
        get() = weakBottomMap?.get()


}