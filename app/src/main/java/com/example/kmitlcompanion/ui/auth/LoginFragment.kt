package com.example.kmitlcompanion.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback

import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentLoginBinding
import com.example.kmitlcompanion.presentation.utils.ActivityNavigation
import com.example.kmitlcompanion.presentation.viewmodel.LoginViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.auth.helpers.AuthHelper
import com.example.kmitlcompanion.ui.mainactivity.utils.BottomBarUtils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(), ActivityNavigation {

    override val layoutId: Int = R.layout.fragment_login

    override val viewModel: LoginViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {

    }

    @Inject lateinit var bottomBarUtils: BottomBarUtils

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


        //(activity as AppCompatActivity?)?.getSupportActionBar()?.hide()
        binding = FragmentLoginBinding.inflate(inflater,container, false).apply {
            viewModel = this@LoginFragment.viewModel
            setupViewObservers()
        }

        //hide bottom bar
//        val bottomNavigationView = requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator_bottom_nav)
//        bottomNavigationView.visibility = View.GONE
        bottomBarUtils.bottomMap?.visibility = View.INVISIBLE

        viewModel.setActivityContext(requireActivity())
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Disable back button functionality here
            }
        })
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

            updateUserRoom.observe(viewLifecycleOwner, Observer {
                helper.updateUserRoom(it!!)
            })

            loginResponse.observe(viewLifecycleOwner, Observer {
                helper.postLogin(it!!)
            })
            signOutFailedResponse.observe(viewLifecycleOwner, Observer {
                helper.signOut_failed()
                helper.showDialog("เกิดข้อผิดพลาด ต้องใช้ Email ของสถาบันในการเข้าใช้งาน",requireView())
            })

            signInHome.observe(viewLifecycleOwner, Observer {
                helper.signInHome()
                helper.showDialog("ลงชื่อเข้าใช้สำเร็จ",requireView())
            })

            signInIdentity.observe(viewLifecycleOwner, Observer {
                helper.signInIdentity()
            })

        }
    }

}