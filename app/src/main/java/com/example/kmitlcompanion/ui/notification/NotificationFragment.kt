package com.example.kmitlcompanion.ui.notification

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentNotificationBinding
import com.example.kmitlcompanion.presentation.NotificationViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseFragment<FragmentNotificationBinding,NotificationViewModel>() {

    override val layoutId: Int = R.layout.fragment_notification

    override val viewModel: NotificationViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNotificationBinding.inflate(inflater,container,false).apply {
            viewModel = this@NotificationFragment.viewModel
        }

        return binding.root
    }



}