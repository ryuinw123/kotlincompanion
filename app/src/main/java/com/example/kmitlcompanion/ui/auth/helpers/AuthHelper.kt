//package com.example.kmitlcompanion.ui.auth.helpers
//
//import android.content.Intent
//import android.util.Log
//import androidx.core.app.ActivityCompat.startActivityForResult
//import androidx.lifecycle.DefaultLifecycleObserver
//import javax.inject.Inject
//
////lib google o auth
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.common.api.ApiException
//import com.google.android.gms.tasks.Task
//
//
//class AuthHelper @Inject constructor() : DefaultLifecycleObserver {
//
//    //google signin
//    val RC_SIGN_IN: Int = 9001
//    lateinit var mGoogleSignInClient: GoogleSignInClient
//    lateinit var mGoogleSignInOptions: GoogleSignInOptions
//
//    val startActivityForResultEvent = LiveMessageEvent<ActivityNavigation>()
//
//    private fun signIn() {
//        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
//        startActivityForResultEvent.sendEvent { startActivityForResult(signInIntent, RC_SIGN_IN) }
//        //startActivityForResult(signInIntent, RC_SIGN_IN)
//
//    }
//
//    //Called from Activity receving result
//    fun onResultFromActivity(requestCode: Int, resultCode: Int, data: Intent?) {
//        when(requestCode) {
//            RC_SIGN_IN -> {
//                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//                handleSignInResult(task)
//            }
//        }
//    }
//
//
////    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
////        super.onActivityResult(requestCode, resultCode, data)
////        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
////        if (requestCode == RC_SIGN_IN) {
////            // The Task returned from this call is always completed, no need to attach
////            // a listener.
////            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
////            handleSignInResult(task)
////        }
////
////
////    }
//
//    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
//        try {
//            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
//            // Signed in successfully, show authenticated UI.
//
//            Log.d("OAuth",account.idToken.toString())
//
//            //val apiService = ApiService()
//            //val tok_ = testToken(token = account.idToken.toString(), validate = false ,auth_userdata = "")
//            //postTestData(apiService,tok_)
//            //updateUI(account)
//
//        } catch (e: ApiException) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w("Error", "signInResult:failed code=" + e.statusCode)
//            //updateUI(null)
//        }
//    }
//
//
//}