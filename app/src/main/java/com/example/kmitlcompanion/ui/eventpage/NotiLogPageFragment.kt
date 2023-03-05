package com.example.kmitlcompanion.ui.eventpage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentNotiLogPageBinding
import com.example.kmitlcompanion.presentation.viewmodel.NotiLogPageViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.eventpage.helper.NotiLogHelper
import com.example.kmitlcompanion.ui.mainactivity.utils.BottomBarUtils
import com.example.kmitlcompanion.ui.mapboxview.helpers.ViewHelper
import com.example.kmitlcompanion.ui.mapboxview.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotiLogPageFragment : BaseFragment<FragmentNotiLogPageBinding,NotiLogPageViewModel>() {

    @Inject internal lateinit var helper: NotiLogHelper
    @Inject lateinit var bottomBarUtils: BottomBarUtils

    override val layoutId: Int = R.id.notiLogPageFragment
    override val viewModel: NotiLogPageViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotiLogPageBinding.inflate(inflater,container,false).apply {
            viewModel = this@NotiLogPageFragment.viewModel
            helper.viewNotiLog.setup(this@NotiLogPageFragment.viewModel,requireContext())
            setupViewObservers()
        }


        //show bottom bar
        bottomBarUtils.bottomMap?.visibility = View.VISIBLE
        return binding.root
    }

    private fun FragmentNotiLogPageBinding.setupViewObservers(){
        this@NotiLogPageFragment.viewModel.run {
            notiLogData.observe(viewLifecycleOwner , Observer {
                Log.d("test_noti_log",it.toString())
            })
        }
    }


}