package com.example.kmitlcompanion.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentSettingsBinding
import com.example.kmitlcompanion.presentation.viewmodel.SettingsViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.editevent.EditEventFragmentArgs
import com.example.kmitlcompanion.ui.mainactivity.utils.BottomBarUtils
import com.example.kmitlcompanion.ui.mapboxview.MapboxFragment
import com.example.kmitlcompanion.ui.settings.helper.SettingHelper
import com.example.kmitlcompanion.ui.settingsedit.SettingsEditFragmentArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>() {

    override val layoutId: Int = R.layout.fragment_settings

    override val viewModel: SettingsViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {
    }

    @Inject lateinit var helper: SettingHelper

    @Inject lateinit var bottomBarUtils: BottomBarUtils



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        helper.setup(viewModel,requireActivity())

        binding = FragmentSettingsBinding.inflate(inflater,container, false).apply {
            viewModel = this@SettingsFragment.viewModel
            setupViewObservers()
        }

        //show bottom navbar
        bottomBarUtils.bottomMap?.visibility = View.VISIBLE
        return binding.root

    }


    private fun FragmentSettingsBinding.setupViewObservers() {
        this@SettingsFragment.viewModel.run {
            updateUserRoom.observe(viewLifecycleOwner, Observer {
               updateUser(it!!)
            })

            signOut.observe(viewLifecycleOwner, Observer {
                helper.signOut()

                //fragmentManager.beginTransaction().remove(MapboxFragment).commitAllowingStateLoss()
                val fragment = MapboxFragment()

                if (fragment != null) {
                    childFragmentManager?.beginTransaction()?.remove(fragment)?.commit()
                }
            })
        }
    }
}