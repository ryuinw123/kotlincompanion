package com.example.kmitlcompanion.ui.identitylogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentIdentityloginBinding
import com.example.kmitlcompanion.presentation.viewmodel.IdentityloginViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.identitylogin.helper.IdentityHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IdentityloginFragment : BaseFragment<FragmentIdentityloginBinding, IdentityloginViewModel>() {

    override val layoutId: Int = R.layout.fragment_identitylogin

    override val viewModel: IdentityloginViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {
    }

    @Inject
    internal lateinit var helper: IdentityHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        helper.setup(viewModel)

        (activity as AppCompatActivity?)?.getSupportActionBar()?.hide()
        binding = FragmentIdentityloginBinding.inflate(inflater,container, false).apply{
            viewModel = this@IdentityloginFragment.viewModel
            setupViewObservers()
        }
        viewModel.setActivityBinding(binding,requireActivity())

        viewModel.listFieldAdapter()
        viewModel.inputListener()

        return binding.root
    }

    private fun FragmentIdentityloginBinding.setupViewObservers(){
        this@IdentityloginFragment.viewModel.run {
            saveUserDataResponse.observe(viewLifecycleOwner,{
                helper.postUserData(it!!)
            })

            getUserRoom.observe(viewLifecycleOwner,{
                helper.getUserRoom(it!!)
            })

            nextHomepage.observe(viewLifecycleOwner, Observer {
                helper.nextHomePage()
            })
        }
    }


}