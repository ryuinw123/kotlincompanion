package com.example.kmitlcompanion.ui.auth.helpers

import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.kmitlcompanion.presentation.LoginViewModel
import javax.inject.Inject

import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
class AuthHelper @Inject constructor() : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.d("auth_helper","onCreate")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Log.d("auth_helper","onStart")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.d("auth_helper","onResume")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Log.d("auth_helper","onPause")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Log.d("auth_helper","onStop")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Log.d("auth_helper","onDestroy")
    }

    private lateinit var viewModel: LoginViewModel

    fun setup(viewModel: LoginViewModel){
        this.viewModel = viewModel
    }

    fun postToken(token: String){
        viewModel.postToken(token)
    }

    fun signOut(){
        viewModel.signOut()
        viewModel.nextPage()
    }

}