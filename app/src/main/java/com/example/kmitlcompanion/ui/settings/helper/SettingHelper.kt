package com.example.kmitlcompanion.ui.settings.helper

import android.app.Activity
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.presentation.viewmodel.SettingsViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class SettingHelper @Inject constructor() : DefaultLifecycleObserver {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var activity: Activity
    lateinit var mGoogleSignInClient: GoogleSignInClient

    fun setup(viewModel: SettingsViewModel,activity: Activity){
        this.viewModel = viewModel
        this.activity = activity

    }

    fun signOut() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.NewAUTH_ID))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)

        mGoogleSignInClient.signOut()
            .addOnCompleteListener(activity, OnCompleteListener<Void?> {
                revokeAccess()
                Log.d("Massage","signOut")
            })
    }

    private fun revokeAccess() {
        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener(activity, OnCompleteListener<Void?> {
                Log.d("Massage","revokeAccess")
                viewModel.updateUser(UserData(id=0,email="",token=""))

                //_updateUserRoom.value = UserData(id=0,email="",token="")

            })
    }
}