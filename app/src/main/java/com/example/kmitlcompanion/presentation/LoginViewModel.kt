package com.example.kmitlcompanion.presentation

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.domain.usecases.postLogin
import com.example.kmitlcompanion.presentation.eventobserver.Event
import com.example.kmitlcompanion.presentation.utils.ActivityNavigation
import com.example.kmitlcompanion.presentation.utils.LiveMessageEvent
import com.example.kmitlcompanion.presentation.utils.SingleLiveData
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
import io.reactivex.rxjava3.observers.DisposableObserver

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postLogin: postLogin
)
    : BaseViewModel() {

    //google signin
    val RC_SIGN_IN: Int = 9001
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var activity: Activity

    val startActivityForResultEvent = LiveMessageEvent<ActivityNavigation>()

    private val _loginResponse = SingleLiveData<String>()
    val loginResponse: SingleLiveData<String> = _loginResponse

    private val _signOutFailedResponse = SingleLiveData<Event<Boolean>>()
    val signOutFailedResponse: SingleLiveData<Event<Boolean>> = _signOutFailedResponse

    private val _signInHome = SingleLiveData<Event<Boolean>>()
    val signInHome: SingleLiveData<Event<Boolean>> = _signInHome

    private val _signInIdentity = SingleLiveData<Event<Boolean>>()
    val signInIdentity: SingleLiveData<Event<Boolean>> = _signInIdentity

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
            Log.d("OAuth",account.idToken!!)

            _loginResponse.value = account.idToken
            //_loginResponse.value = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImVkMzZjMjU3YzQ3ZWJhYmI0N2I0NTY4MjhhODU4YWE1ZmNkYTEyZGQiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI1NjM1MDkwMDIwODQtMzVwMjdvZG85anFhcm8yaGRnbzJuZG1vMjE1aWVuajAuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI1NjM1MDkwMDIwODQtYjdtMDVib2lhcXM1bW8wdGhpNGthNTlub2lha2V1czIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTg0MzIwNDk5MTAwMjYyOTA5NjIiLCJoZCI6ImttaXRsLmFjLnRoIiwiZW1haWwiOiI2MjAxMDg5M0BrbWl0bC5hYy50aCIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiNjJfMDg5MyBTVVBIQU5VVCBXQU5ERUUiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUxtNXd1MUVWbHItbm95d3NhRG4tbUZDWGk2SkpkQ3JYOU82b1JaMEVNZz1zOTYtYyIsImdpdmVuX25hbWUiOiI2Ml8wODkzIFNVUEhBTlVUIiwiZmFtaWx5X25hbWUiOiJXQU5ERUUiLCJsb2NhbGUiOiJ0aCIsImlhdCI6MTY2NTMzMDk5NCwiZXhwIjoxNjY1MzM0NTk0fQ.ppgbS5FfMhSQk1J-iu3DVIx0DVaCc3TLbdEcByZ3RAkPkRj8HvPgVjmXVbbENmPpI2WNVP19IVJ-l_H08-f9zgpylCiSRj-jXod4m7IKx9FqfmHZtyQ-YdUl_Sej3XSYNicNJsl9axzXBG-ZUEtRUBHcpmG3Oh9Zl4jmijT53w0BkGoQetn5vLnk_NeRkTApLE6ye04IqOBOsN-dWr7nWsTIKnOZT4TqUCO8MLdRB2DP9qxcJQTLw2nzlWTNIpOLSB5EwtH22X4hVktVZtA3nCfkadCTwOsoT6BpDz28-e_R-CNMoqu4__XEdIqBZ5bKOCk1xzxL-_Jl8D7hFAy0PQ"
            //_loginResponse.postValue(null)

        } catch (e: ApiException) {
            Log.w("Error", "signInResult:failed code=" + e.statusCode)
        }
    }


    /************ api ************************/

    fun postLogin(token : String){
        postLogin.execute(object : DisposableObserver<Int>() {
            override fun onComplete() {
                Log.d("AUTH","Success")
            }

            override fun onNext(t: Int) {
                if (t == 1){
                    _signInHome.value = Event(true)
                }else if(t == 0){
                    _signInIdentity.value = Event(true)
                }
            }

            override fun onError(e: Throwable) {
                Log.d("AUTH",e.toString())
                _signOutFailedResponse.value = Event(true)
            }
        }, token)
    }


    /********* navigate function ***********/

    fun nextHomePage(){
        navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
    }

    fun nextIdentityPage(){
        navigate(LoginFragmentDirections.actionLoginFragmentToIdentityloginFragment())
    }

    /****** sign out ******/

    fun signOut() {
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



    /**************/



}