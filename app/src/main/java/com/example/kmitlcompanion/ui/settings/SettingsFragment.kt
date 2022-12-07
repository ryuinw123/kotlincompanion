package com.example.kmitlcompanion.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentSettingsBinding
import com.example.kmitlcompanion.presentation.viewmodel.SettingsViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.settings.helper.SettingHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>() {

    override val layoutId: Int = R.layout.fragment_settings

    override val viewModel: SettingsViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {
    }
    @Inject
    internal lateinit var helper: SettingHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        helper.setup(viewModel)

        binding = FragmentSettingsBinding.inflate(inflater,container, false).apply {
            viewModel = this@SettingsFragment.viewModel
            setupViewObservers()
        }

        viewModel.setActivityContext(requireActivity())

        //show bottom navbar
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.VISIBLE
        return binding.root
    }


    private fun FragmentSettingsBinding.setupViewObservers() {
        this@SettingsFragment.viewModel.run {
            updateUserRoom.observe(viewLifecycleOwner, Observer {
                helper.updateUserRoom(it!!)
            })
        }
    }
}