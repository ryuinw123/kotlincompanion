package com.example.kmitlcompanion.ui.identitylogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentIdentityloginBinding
import com.example.kmitlcompanion.presentation.viewmodel.IdentityloginViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.identitylogin.helper.IdentityHelper
import com.example.kmitlcompanion.ui.mainactivity.utils.BottomBarUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IdentityloginFragment : BaseFragment<FragmentIdentityloginBinding, IdentityloginViewModel>() {

    override val layoutId: Int = R.layout.fragment_identitylogin

    override val viewModel: IdentityloginViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {
    }
    @Inject lateinit var bottomBarUtils: BottomBarUtils

    @Inject
    internal lateinit var helper: IdentityHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIdentityloginBinding.inflate(inflater,container, false).apply{
            viewModel = this@IdentityloginFragment.viewModel

            helper.setup(this@IdentityloginFragment.viewModel, requireContext(), requireActivity(),textNameValue,textSurnameValue
                ,facultyList,departmentList,yearList,textinputName,textinputSurname,faculty,department,year,signinButton)

            setupViewObservers()
            setupListener()
        }
        //viewModel.setActivityBinding(binding,requireActivity())

//        viewModel.listFieldAdapter()
//        viewModel.inputListener()

        bottomBarUtils.bottomMap?.visibility = View.INVISIBLE
        return binding.root
    }

    private fun FragmentIdentityloginBinding.setupViewObservers(){
        this@IdentityloginFragment.viewModel.run {
            saveUserDataResponse.observe(viewLifecycleOwner,Observer{
                postUserData(it)
            })

//            nextHomepage.observe(viewLifecycleOwner, Observer {
//                nextHomePage()
//            })
        }
    }

    private fun FragmentIdentityloginBinding.setupListener(){
        helper.inputListener()

        textNameValue?.doAfterTextChanged {
            nameValue = textNameValue?.text?.toString()?.replace(" ", "")
            helper.checkAllFieldValid()
        }

        textSurnameValue?.doAfterTextChanged {
            surnameValue = textSurnameValue?.text?.trim().toString()?.replace(" ", "")
            helper.checkAllFieldValid()
        }

        facultyList?.doAfterTextChanged{
            facultyValue = facultyList?.text?.toString()
            helper.checkAllFieldValid()
        }

        departmentList?.doAfterTextChanged {
            departmentValue = departmentList?.text?.toString()
            helper.checkAllFieldValid()
        }

        yearList?.doAfterTextChanged {
            yearValue = yearList?.text?.toString()
            helper.checkAllFieldValid()
        }

    }


}