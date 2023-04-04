package com.example.kmitlcompanion.ui.startpage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.background.LocationService
import com.example.kmitlcompanion.databinding.FragmentStartPageBinding
import com.example.kmitlcompanion.presentation.viewmodel.StartPageViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.mainactivity.MainActivity
import com.example.kmitlcompanion.ui.mainactivity.utils.BottomBarUtils
import com.example.kmitlcompanion.ui.startpage.helper.StartPageFragmentHelper
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartPageFragment : BaseFragment<FragmentStartPageBinding, StartPageViewModel>(){

    override val layoutId: Int = R.layout.fragment_start_page

    override val viewModel: StartPageViewModel by viewModels()


    @Inject
    internal lateinit var helper: StartPageFragmentHelper
    @Inject lateinit var bottomBarUtils: BottomBarUtils

    override fun onReady(savedInstanceState: Bundle?) {

        //val locationId = arguments?.getLong("locationId") ?: -1L
        //Log.d("Geofence","locationId From StartFragment = $locationId")

        val intent = activity?.intent
        val eventId : Long = intent?.extras?.getLong("locationId") ?: 0
        viewModel.updateEventId(eventId)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        helper.setup(viewModel)
        binding = FragmentStartPageBinding.inflate(inflater, container, false).apply {
            viewModel = this@StartPageFragment.viewModel
            setupViewObservers()
        }


//        helper.checkPermission(requireContext(),requireActivity()) {
//            Log.d("test_permsiss","calleld back")
//        viewModel.loginWithToken()
//        }

        //hide bottom bar
        bottomBarUtils.bottomMap?.visibility = View.INVISIBLE

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

    override fun onResume() {
        super.onResume()
        helper.checkPermission(requireContext(),requireActivity()){
            viewModel.loginWithToken()
        }
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