package com.example.kmitlcompanion.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentLoginBinding
import com.example.kmitlcompanion.presentation.ActivityNavigation
import com.example.kmitlcompanion.presentation.LoginViewModel
import com.example.kmitlcompanion.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding,LoginViewModel>(),ActivityNavigation {

    override val layoutId: Int = R.layout.fragment_login

    override val viewModel: LoginViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {

    }
    //private val viewModel: LoginViewModel by viewModels()
    //private lateinit var binding: FragmentLoginBinding

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

}