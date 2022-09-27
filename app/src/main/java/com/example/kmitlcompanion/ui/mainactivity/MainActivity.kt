package com.example.kmitlcompanion.ui.mainactivity

import android.os.Bundle
import androidx.databinding.DataBindingUtil.setContentView as bindingSetContentView
import androidx.appcompat.app.AppCompatActivity
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSetContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }



}
