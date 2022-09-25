package com.example.kmitlcompanion.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentSignInBinding
import com.example.kmitlcompanion.viewmodels.SignInViewModel

class SignIn : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        val button = view.findViewById<Button>(R.id.signInButton)


        button.setOnClickListener{
            findNavController().navigate(R.id.action_signIn_to_home)
        }

        return view
    }

//    companion object {
//        fun newInstance() = SignIn()
//    }


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