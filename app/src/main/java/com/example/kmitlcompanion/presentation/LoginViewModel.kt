package com.example.kmitlcompanion.presentation

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.ui.auth.LoginFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

@HiltViewModel
class LoginViewModel @Inject constructor()
    : BaseViewModel() {

    //google signin
    val RC_SIGN_IN: Int = 9001
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var activity: Activity

    val startActivityForResultEvent = LiveMessageEvent<ActivityNavigation>()

    fun setActivityContext(act: Activity){
        activity = act
    }

    fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.NewAUTH_ID))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
         val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        startActivityForResultEvent.sendEvent { startActivityForResult(signInIntent, RC_SIGN_IN) }
    }

    //Called from Activity receving result
    fun onResultFromActivity(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            RC_SIGN_IN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            Log.d("OAuth",account.idToken.toString())
            signOut()//*******************
            nextPage()

//            val apiService = ApiService()
//            val tok_ = testToken(token = account.idToken.toString(), validate = false ,auth_userdata = "")
//            postTestData(apiService,tok_)
            //updateUI(account)

        } catch (e: ApiException) {
            Log.w("Error", "signInResult:failed code=" + e.statusCode)
        }
    }

    /********* navigate function ***********/

    fun nextPage(){Log.d("Massage","NextPage")
        navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
    }



    /****** sign out ******/

    private fun signOut() {
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
            })
    }

}