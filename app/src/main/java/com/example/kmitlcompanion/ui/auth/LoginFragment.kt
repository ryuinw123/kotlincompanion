package com.example.kmitlcompanion.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentLoginBinding
import com.example.kmitlcompanion.presentation.ActivityNavigation
import com.example.kmitlcompanion.presentation.LoginViewModel
import com.example.kmitlcompanion.ui.mainfragment.MainFragmentDirections
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(),ActivityNavigation {

    //lateinit var mGoogleSignInClient: GoogleSignInClient
    //lateinit var mGoogleSignInOptions: GoogleSignInOptions

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeUi()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        (activity as AppCompatActivity?)?.getSupportActionBar()?.hide()
        binding = FragmentLoginBinding.inflate(inflater,container, false).apply {
            viewModel = this@LoginFragment.viewModel
        }

        viewModel.setActivityContext(requireActivity())


//        binding.signinButton.setOnClickListener{
//
//            //auth
//            //check user first login? --> call -->
//            view?.findNavController()?.navigate(LoginFragmentDirections.actionLoginFragmentToIdentityloginFragment())
//            //else
//            //view?.findNavController()?.navigate(LoginFragmentDirections.actionLoginFragmentToMapboxFragment2())
//
//        }

        return binding.root
    }


    private fun subscribeUi() {
        //this sets the LifeCycler owner and receiver
        viewModel.startActivityForResultEvent.setEventReceiver(this,this)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.onResultFromActivity(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
    }

//    companion object {
//        fun newInstance() = LoginFragment()
//    }
//

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
//        // TODO: Use the ViewModel
//    }



}