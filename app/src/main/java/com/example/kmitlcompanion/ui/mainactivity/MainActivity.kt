package com.example.kmitlcompanion.ui.mainactivity

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
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
import com.example.kmitlcompanion.ui.mainactivity.utils.BottomBarUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.karumi.dexter.Dexter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.databinding.DataBindingUtil.setContentView as bindingSetContentView


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var helper : NavHelper
    @Inject lateinit var bottomBarUtils: BottomBarUtils

    private lateinit var binding: ActivityMainBinding

    private lateinit var navHostF: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingSetContentView<ActivityMainBinding?>(this, R.layout.activity_main).apply {
            helper = this@MainActivity.helper
            navHostF = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

            this@MainActivity.helper.setup(this@MainActivity,window,navHostF.navController,root, bottomNavigation, bottomMap)
            bottomBarUtils.setup(coordinatorBottomNav)

        }

       val locationId : Long = intent.extras?.getLong("locationId") ?: -1

        Log.d("Geofence" , "LocationId = $locationId")

        if (locationId != -1L) {
            val bundle = Bundle()
            bundle.putLong("locationId", locationId)
            val navController = binding.navHostFragment?.getFragment<NavHostFragment>()?.navController
            navController?.setGraph(R.navigation.nav_graph,bundle)
            Log.d("Geofence","Notification LocationId = $locationId")
        }

        //setContentView(R.layout.activity_main)
    }





}
