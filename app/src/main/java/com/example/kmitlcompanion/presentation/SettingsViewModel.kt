package com.example.kmitlcompanion.presentation

import android.app.Activity
import android.util.Log
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.domain.usecases.UpdateUser
import com.example.kmitlcompanion.presentation.utils.SingleLiveData
import com.example.kmitlcompanion.ui.settings.SettingsFragmentDirections
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import javax.inject.Inject
import com.google.android.gms.tasks.OnCompleteListener
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.observers.DisposableCompletableObserver

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val updateUser: UpdateUser
    ) : BaseViewModel() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var activity: Activity


    //update user data in room
    private val _updateUserRoom = SingleLiveData<UserData>()
    val updateUserRoom: SingleLiveData<UserData> = _updateUserRoom


    fun setActivityContext(act: Activity){
        activity = act
    }

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
                _updateUserRoom.value = UserData(id=0,email="",token="")
            })
    }

    //room update user
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
}