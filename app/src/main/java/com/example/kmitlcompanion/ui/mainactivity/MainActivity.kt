package com.example.kmitlcompanion.ui.mainactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import androidx.databinding.DataBindingUtil.setContentView as bindingSetContentView


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        bindingSetContentView<ActivityMainBinding>(this, R.layout.activity_main)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation) as BottomNavigationView
        //val buttonNavigationMap = findViewById<BottomNavigationView>(R.id.map_button_navigation) as BottomNavigationView
        setupWithNavController(bottomNavigationView,navController)

        val mapButtonFragment = findViewById<FloatingActionButton>(R.id.map_button)
        mapButtonFragment.setOnClickListener {
            bottomNavigationView.selectedItemId = R.id.mapboxFragment2
        }

    }

}
