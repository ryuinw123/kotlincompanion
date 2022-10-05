package com.example.kmitlcompanion.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.kmitlcompanion.databinding.FragmentLoginBinding
import com.example.kmitlcompanion.presentation.LoginViewModel
import com.example.kmitlcompanion.ui.mainfragment.MainFragmentDirections

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)?.getSupportActionBar()?.hide()
        binding = FragmentLoginBinding.inflate(inflater,container, false)

        binding.signinButton.setOnClickListener{
            view?.findNavController()?.navigate(LoginFragmentDirections.actionLoginFragmentToMapboxFragment2())
        }

        return binding.root
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