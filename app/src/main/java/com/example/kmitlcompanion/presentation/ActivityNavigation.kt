package com.example.kmitlcompanion.presentation

import android.content.Intent

interface ActivityNavigation {
    fun startActivityForResult(intent: Intent?, requestCode: Int)
}