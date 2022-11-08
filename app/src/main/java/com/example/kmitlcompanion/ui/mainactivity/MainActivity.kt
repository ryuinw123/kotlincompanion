package com.example.kmitlcompanion.ui.mainactivity

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil.bind
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.ActivityMainBinding
import com.example.kmitlcompanion.ui.mainactivity.helper.NavHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.databinding.DataBindingUtil.setContentView as bindingSetContentView


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var helper : NavHelper

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = bindingSetContentView<ActivityMainBinding?>(this, R.layout.activity_main).apply {
            helper = this@MainActivity.helper
            this@MainActivity.helper.setup(window,navHostFragment,root, bottomNavigation, bottomMap)
        }
        /*this.appearanceLightNavigationBars()
        this.setDrawUIonTop()
        this.setWindowInsetsController()
        this.drawBottomNavBar()*/

    }

    /*private fun setWindowInsetsController(){
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsCompat = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsCompat.hide(WindowInsetsCompat.Type.statusBars())
        windowInsetsCompat.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    private fun appearanceLightNavigationBars(){
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView)

        windowInsetsController?.isAppearanceLightNavigationBars = true
    }

    private fun setDrawUIonTop(){

        val root = findViewById<ConstraintLayout>(R.id.root)
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

    private fun drawBottomNavBar(){

        navController = binding.navHostFragment.getFragment<NavHostFragment>().navController
        val bottomNavigationView = binding.bottomNavigation//findViewById<BottomNavigationView>(R.id.bottom_navigation) as BottomNavigationView
        setupWithNavController(bottomNavigationView,navController)

        val buttonNavigationMap = binding.bottomMap//findViewById<BottomNavigationView>(R.id.botton_map) as BottomNavigationView
        setupWithNavController(buttonNavigationMap,navController)

        val mapButtonFragment = binding.mapButton//findViewById<FloatingActionButton>(R.id.map_button)
        mapButtonFragment.setOnClickListener {
            buttonNavigationMap.selectedItemId = R.id.mapboxFragment2
        }
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN



    }*/


}
