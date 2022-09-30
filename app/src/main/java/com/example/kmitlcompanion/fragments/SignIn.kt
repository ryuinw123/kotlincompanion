package com.example.kmitlcompanion.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.data.api.ApiService
import com.example.kmitlcompanion.data.test
import com.example.kmitlcompanion.databinding.FragmentSignInBinding
import com.example.kmitlcompanion.viewmodels.SignInViewModel


//lib for google signIn
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignIn : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var viewModel: SignInViewModel

    //google signin
    val RC_SIGN_IN: Int = 9001
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    //api service
    //private lateinit var apiService :ApiService


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        //val context = MainActivity.appContext

        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        val button = view.findViewById<Button>(R.id.signInButton)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //.requestIdToken(getString(R.string.AUTH_ID))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        ///////////////////// test api
        val apiService = ApiService()

        //getTestData(apiService)
        postTestData(apiService)


        button.setOnClickListener{
            signIn()
        }

        return view
    }

    fun updateUI(account: GoogleSignInAccount){
        //send to backend to check token
        val token = account.idToken
        val islogin = true

        if (islogin){
            Log.d("Message", "SignIn")
            findNavController().navigate(R.id.action_signIn_to_home)
        }else{
            Log.w("Error", "Token หมดอายุจ้า")
        }

    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            updateUI(account)

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.statusCode)
            //updateUI(null)
        }
    }

    //api function
    fun getTestData(apiService: ApiService){
        val call = apiService.getTest()

        call.enqueue(object: Callback<test>{
            override fun onResponse(call: Call<test>, response: Response<test>) {
                Log.d("Api",response.isSuccessful.toString())
                if(response.isSuccessful){
                    val list = response.body()
                    Log.i("Api","helloooooooooooooooooooo api")
                }
            }

            override fun onFailure(call: Call<test>, t: Throwable) {
                Log.e("API",t.localizedMessage)
            }
        })
    }

    private fun postTestData(apiService: ApiService){
        val call = apiService.postTest("film","banana")

        call.enqueue(object: Callback<test>{
            override fun onResponse(call: Call<test>, response: Response<test>) {
                Log.d("Api",response.isSuccessful.toString())
                if(response.isSuccessful){
                    Log.i("Api","helloooooooooooooooooooo api")
                }
            }

            override fun onFailure(call: Call<test>, t: Throwable) {
                Log.e("API",t.localizedMessage)
            }
        })
    }

    companion object {
        fun newInstance() = SignIn()
    }


/*
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)
        // TODO: Use the ViewModel
    }*/

}