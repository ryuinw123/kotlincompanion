package com.example.kmitlcompanion.ui.startpage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentStartPageBinding
import com.example.kmitlcompanion.presentation.StartPageViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.startpage.helper.StartPageFragmentHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartPageFragment : BaseFragment<FragmentStartPageBinding,StartPageViewModel>(){

    override val layoutId: Int = R.layout.fragment_start_page

    override val viewModel: StartPageViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    @Inject
    internal lateinit var helper: StartPageFragmentHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        helper.setup(viewModel)
        binding = FragmentStartPageBinding.inflate(inflater, container, false).apply {
            viewModel = this@StartPageFragment.viewModel
            setupViewObservers()
        }

        viewModel.loginWithToken()


        //hide bottom bar
        val bottomNavigationView = requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator_bottom_nav)
        bottomNavigationView.visibility = View.GONE

        return binding.root
    }

    private fun FragmentStartPageBinding.setupViewObservers() {
        this@StartPageFragment.viewModel.run {

            updateUserRoom.observe(viewLifecycleOwner, Observer {
                helper.updateUserRoom(it!!)
            })

            getUserRoom.observe(viewLifecycleOwner,Observer{
                helper.getUserRoom()
            })

            loginResponse.observe(viewLifecycleOwner, Observer {
                helper.postLogin(it!!)
            })
        }
    }

}