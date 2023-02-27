package com.example.kmitlcompanion.ui

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.kmitlcompanion.BR
import com.example.kmitlcompanion.presentation.BaseViewModel
import com.example.kmitlcompanion.presentation.navigation.NavigationCommand
import com.example.kmitlcompanion.utils.observeNonNull


abstract class BaseFragment<BINDING : ViewDataBinding,VM : BaseViewModel>(): Fragment() {

    @get:LayoutRes
    protected abstract val layoutId: Int

    protected abstract val viewModel: VM

    protected lateinit var binding: BINDING

    protected abstract fun onReady(savedInstanceState: Bundle?)

    private var navOptions = NavOptions.Builder()
                            .setLaunchSingleTop(true)
                            .setEnterAnim(com.example.kmitlcompanion.R.anim.fade_in_kmitl) // set enter animation
                            .setExitAnim(com.example.kmitlcompanion.R.anim.fade_out_kmitl) // set exit animation
                            .setPopEnterAnim(com.example.kmitlcompanion.R.anim.fade_in_kmitl) // set pop enter animation
                            .setPopExitAnim(com.example.kmitlcompanion.R.anim.fade_out_kmitl) // set pop exit animation
                            .build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            layoutId,
            container,
            false
        )

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.viewModel,viewModel)
        }




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNavigation()

        onReady(savedInstanceState)
    }

    private fun observeNavigation() {
        viewModel.navigation.observeNonNull(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { navigationCommand ->

                handleNavigation(navigationCommand,navOptions)
            }
        }
        viewModel.navigationOption.observeNonNull(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { navigationObject ->
                handleNavigation(navigationObject.first,navigationObject.second)
            }
        }
    }

    private fun handleNavigation(navCommand: NavigationCommand,navOptions: NavOptions) {
        when (navCommand) {
            is NavigationCommand.ToDirection -> findNavController().navigate(navCommand.directions,navOptions)
            is NavigationCommand.Back -> findNavController().navigateUp()
        }
    }

}