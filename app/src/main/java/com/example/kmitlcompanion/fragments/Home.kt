package com.example.kmitlcompanion.fragments


//google signin

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kmitlcompanion.MainActivity
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.viewmodels.HomeViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener


class Home : Fragment() {

    lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_home, container, false)
        val button = view.findViewById<Button>(R.id.signOutButton)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //.requestIdToken(getString(R.string.AUTH_ID))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        button.setOnClickListener{
            signOut()
            revokeAccess()
        }

        return view
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(requireActivity(), OnCompleteListener<Void?> {
                Log.d("Massage","signOut")
            })
    }

    private fun revokeAccess() {
        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener(requireActivity(), OnCompleteListener<Void?> {
                Log.d("Massage","revokeAccess")
                findNavController().navigate(R.id.action_home_to_signIn)
            })
    }

//    companion object {
//        fun newInstance() = Home()
//    }
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}