package com.example.kmitlcompanion.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentHomeBinding
import com.example.kmitlcompanion.presentation.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {


    override val layoutId:Int = R.layout.fragment_home

    override val viewModel: HomeViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {

    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        binding = FragmentHomeBinding.inflate(inflater,container, false).apply {
//            viewModel = this@HomeFragment.viewModel
//        }
//
//        viewModel.setActivityContext(requireActivity())
//
//        //show bottom nav bar
//        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
//        bottomNavigationView.visibility = View.VISIBLE
//
//        return binding.root
//
//    }

}