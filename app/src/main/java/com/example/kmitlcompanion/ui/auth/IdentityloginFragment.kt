package com.example.kmitlcompanion.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentIdentityloginBinding
import com.example.kmitlcompanion.presentation.IdentityloginViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.auth.helpers.IdentityHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IdentityloginFragment : BaseFragment<FragmentIdentityloginBinding,IdentityloginViewModel>() {

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

        val facultyList = arrayListOf<String>("Engineer","Engineer1","Engineer2")
        val arrayAdapter1 = ArrayAdapter<String>(requireActivity(),R.layout.dropdown_item,facultyList)
        binding.facultyList.setAdapter(arrayAdapter1)


        val departmentList = arrayListOf<String>("CE","EE","CPE")
        val arrayAdapter2 = ArrayAdapter<String>(requireActivity(),R.layout.dropdown_item,departmentList)
        binding.departmentList.setAdapter(arrayAdapter2)

        val yearList = arrayListOf<Int>(1,2,3,4,5,6,7,8)
        val arrayAdapter3 = ArrayAdapter<Int>(requireActivity(),R.layout.dropdown_item,yearList)
        binding.yearList.setAdapter(arrayAdapter3)

        return binding.root
    }


    private fun FragmentIdentityloginBinding.setupViewObservers(){
        this@IdentityloginFragment.viewModel.run {
            saveUserDataResponse.observe(viewLifecycleOwner,{
                helper.postUserData(it!!)
            })
            nextHomepage.observe(viewLifecycleOwner, Observer {
                helper.nextHomePage()
            })
        }
    }

//
//    companion object {
//        fun newInstance() = IdentityloginFragment()
//    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(IdentityloginViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}