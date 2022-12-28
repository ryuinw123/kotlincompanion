package com.example.kmitlcompanion.presentation.viewmodel

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.data.model.ReturnLoginData
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.domain.usecases.PostLogin
import com.example.kmitlcompanion.domain.usecases.UpdateUser
import com.example.kmitlcompanion.presentation.BaseViewModel
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
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.observers.DisposableObserver

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postLogin: PostLogin,
    private val updateUser: UpdateUser,
)
    : BaseViewModel() {

    //google signin
    val RC_SIGN_IN: Int = 9001
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var activity: Activity

    //google oauth
    val startActivityForResultEvent = LiveMessageEvent<ActivityNavigation>()

    //update user data in room
    private val _updateUserRoom = SingleLiveData<UserData>()
    val updateUserRoom: SingleLiveData<UserData> = _updateUserRoom

    //login
    private val _loginResponse = SingleLiveData<String>()
    val loginResponse: SingleLiveData<String> = _loginResponse

    //navigation login field because not kmitl account
    private val _signOutFailedResponse = SingleLiveData<Event<Boolean>>()
    val signOutFailedResponse: SingleLiveData<Event<Boolean>> = _signOutFailedResponse

    //navigaion login success to homepage
    private val _signInHome = SingleLiveData<Event<Boolean>>()
    val signInHome: SingleLiveData<Event<Boolean>> = _signInHome

    //navigation login success to identitypage
    private val _signInIdentity = SingleLiveData<Event<Boolean>>()
    val signInIdentity: SingleLiveData<Event<Boolean>> = _signInIdentity

    fun setActivityContext(act: Activity){
        activity = act
    }

    fun signIn() {
        //val profileScope = Scope("https://www.googleapis.com/auth/userinfo.profile")
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //.requestIdToken(activity.getString(R.string.NewAUTH_ID))
            .requestEmail()
            .requestServerAuthCode(activity.getString(R.string.NewAUTH_ID),false)
            .requestProfile()
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
            //Log.d("OAuth",account.idToken!!)
            Log.d("OAuth Authorization server",account.serverAuthCode!!)

            _loginResponse.value = account.serverAuthCode
            //_updateUserRoom.value = UserData(id=0,email=account.email.toString(),token=account.serverAuthCode.toString())
            //_loginResponse.postValue(null)

        } catch (e: ApiException) {
            Log.w("Error", "signInResult:failed code=" + e.statusCode)
        }
    }


    /************ api ************************/

    fun postLogin(authCode : String){
        postLogin.execute(object : DisposableObserver<ReturnLoginData>() {
            override fun onComplete() {
                Log.d("AUTH","Success")
            }

            override fun onNext(t: ReturnLoginData) {

                _updateUserRoom.value = UserData(id=0,email=t.email,token=t.refreshToken)

                if (t.status == 1){
                    _signInHome.value = Event(true)
                }else if(t.status == 0){
                    _signInIdentity.value = Event(true)
                }
            }

            override fun onError(e: Throwable) {
                Log.d("AUTH",e.toString())
                _signOutFailedResponse.value = Event(true)
            }
        }, authCode)
    }

    /**** save token to data store ****/

    fun updateUser(userData: UserData) {
        updateUser.execute(object : DisposableCompletableObserver(){
            override fun onComplete() {
                Log.d("UpdateUser","Update UserData Complete!!!")
            }

            override fun onError(e: Throwable) {
                Log.d("UpdateUser",e.toString())
            }
        },userData)
    }

    /********* navigate function ***********/

    fun nextHomePage(){
        navigate(LoginFragmentDirections.actionLoginFragmentToMapboxFragment2())
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