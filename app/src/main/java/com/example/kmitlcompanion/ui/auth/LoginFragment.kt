package com.example.kmitlcompanion.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentLoginBinding
import com.example.kmitlcompanion.presentation.utils.ActivityNavigation
import com.example.kmitlcompanion.presentation.LoginViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.auth.helpers.AuthHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(), ActivityNavigation {

    override val layoutId: Int = R.layout.fragment_login

    override val viewModel: LoginViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {

    }

    @Inject
    internal lateinit var helper: AuthHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeUi()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        helper.setup(viewModel)


        (activity as AppCompatActivity?)?.getSupportActionBar()?.hide()
        binding = FragmentLoginBinding.inflate(inflater,container, false).apply {
            viewModel = this@LoginFragment.viewModel
            setupViewObservers()
        }

        viewModel.setActivityContext(requireActivity())
        return binding.root
    }


    private fun subscribeUi() {
        viewModel.startActivityForResultEvent.setEventReceiver(this,this)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.onResultFromActivity(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun FragmentLoginBinding.setupViewObservers(){
        this@LoginFragment.viewModel.run {
            loginResponse.observe(viewLifecycleOwner, Observer {
                helper.postLogin(it!!)
            })
            signOutFailedResponse.observe(viewLifecycleOwner, Observer {
                helper.signOut_failed()
            })

            signInHome.observe(viewLifecycleOwner, Observer {
                helper.signInHome()
            })

            signInIdentity.observe(viewLifecycleOwner, Observer {
                helper.signInIdentity()
            })

        }
    }

}