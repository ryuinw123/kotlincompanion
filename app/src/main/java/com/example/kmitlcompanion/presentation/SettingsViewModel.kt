package com.example.kmitlcompanion.presentation

import android.app.Activity
import android.util.Log
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.ui.settings.SettingsFragmentDirections
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import javax.inject.Inject
import com.google.android.gms.tasks.OnCompleteListener

class SettingsViewModel @Inject constructor() : BaseViewModel() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var activity: Activity

    fun setActivityContext(act: Activity){
        activity = act
    }

//    fun gotoMap(){
//        navigate(HomeFragmentDirections.actionHomeFragmentToMapboxFragment2())
//    }

    fun gotoLogin(){
        Log.d("Logout","Logout")
        navigate(SettingsFragmentDirections.actionSettingsFragmentToLoginFragment())
    }

    fun goBackClicked() {
        navigateBack()
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
                gotoLogin()
            })
    }
}