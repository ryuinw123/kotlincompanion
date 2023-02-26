package com.example.kmitlcompanion.ui.mainactivity.helper

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.ui.mapboxview.MapboxFragmentDirections
import com.example.kmitlcompanion.ui.settings.SettingsFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.ref.WeakReference
import javax.inject.Inject

class  NavHelper  @Inject constructor(

){

    private var weakNavHostFragment: WeakReference<NavHostFragment>? = null
    private var weakBottomMap : WeakReference<BottomNavigationView>? = null
    private var weakBottomNavigation : WeakReference<BottomNavigationView>? = null
    private lateinit var navController: NavController
    private lateinit var activity: Activity

    //FragmentContainerView
    fun setup(activity: Activity,window: Window,
              navController: NavController,
              root : ConstraintLayout,
              bottomNavigation : BottomNavigationView,
              bottomMap: BottomNavigationView){

        this.activity = activity
        this.weakNavHostFragment = WeakReference(navHostFragment)

        this.weakBottomMap = WeakReference(bottomMap)
        this.weakBottomNavigation = WeakReference(bottomNavigation)

        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.fade_in_kmitl) // set enter animation
            .setExitAnim(R.anim.fade_out_kmitl) // set exit animation
            .setPopEnterAnim(R.anim.fade_in_kmitl) // set pop enter animation
            .setPopExitAnim(R.anim.fade_out_kmitl) // set pop exit animation
            .build()

        bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bottomBarSettingsFragment -> navController.navigate(R.id.settingsFragment,null,navOptions)
                R.id.bottomBarNotficationLogFragment -> navController.navigate(R.id.eventPageFragment,null,navOptions)
            }
            true
        }

        bottomMap.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bottomBarMapboxFragment -> navController.navigate(R.id.mapboxFragment,null,navOptions)
            }
            true
        }

    }

//    private fun appearanceLightNavigationBars(window : Window){
//
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        val windowInsetsController =
//            ViewCompat.getWindowInsetsController(window.decorView)
//
//        windowInsetsController?.isAppearanceLightNavigationBars = true
//    }
//
//    private fun setDrawUIonTop(root: ConstraintLayout){
//
//        ViewCompat.setOnApplyWindowInsetsListener(
//            root
//        ) { view: View, windowInsets: WindowInsetsCompat ->
//            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
//            view.layoutParams = (view.layoutParams as FrameLayout.LayoutParams).apply {
//                // draw on top of the bottom navigation bar
//                bottomMargin = insets.bottom
//            }
//
//            WindowInsetsCompat.CONSUMED
//        }
//        /////////////////////////////////////////
//    }

//    private fun setWindowInsetsController(window: Window){
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        val windowInsetsCompat = WindowInsetsControllerCompat(window, window.decorView)
//        windowInsetsCompat.hide(WindowInsetsCompat.Type.statusBars())
//        windowInsetsCompat.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//    }

    fun navigateToMap() {
        Log.d("test_debug","navigate")
        bottomMap?.selectedItemId = R.id.bottomBarMapboxFragment
    }

    private val navHostFragment
        get() = weakNavHostFragment?.get()
    private val bottomMap
        get() = weakBottomMap?.get()
    private val bottomNavigation
        get() = weakBottomNavigation?.get()
}