package com.example.kmitlcompanion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kmitlcompanion.fragments.SignIn

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                //.replace(R.id.container, MainFragment.newInstance())
                .replace(R.id.container, SignIn.newInstance())
                .commitNow()
        }
    }


}