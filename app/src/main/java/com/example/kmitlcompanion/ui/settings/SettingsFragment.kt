package com.example.kmitlcompanion.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentSettingsBinding
import com.example.kmitlcompanion.presentation.SettingsViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding,SettingsViewModel>() {

    override val layoutId: Int = R.layout.fragment_settings

    override val viewModel: SettingsViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSettingsBinding.inflate(inflater,container, false).apply {
            viewModel = this@SettingsFragment.viewModel
        }

        viewModel.setActivityContext(requireActivity())

        //show bottom navbar
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.VISIBLE
        return binding.root
    }

}