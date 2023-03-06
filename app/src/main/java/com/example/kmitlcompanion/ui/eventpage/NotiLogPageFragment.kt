package com.example.kmitlcompanion.ui.eventpage

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
            helper.viewNotiLog.setup(this@NotiLogPageFragment.viewModel,requireContext(),recyclerView)
            //helper.viewClearItem.setup(requireContext(),this@NotiLogPageFragment.viewModel,recyclerView)
            setupViewObservers()
        }

        //show bottom bar
        bottomBarUtils.bottomMap?.visibility = View.VISIBLE
        return binding.root
    }

    private fun FragmentNotiLogPageBinding.setupViewObservers(){
        this@NotiLogPageFragment.viewModel.run {

            notiLogData.observe(viewLifecycleOwner , Observer {
                helper.viewNotiLog.update(it)

                if (it.isEmpty()){
                    notiEmpty.visibility = View.VISIBLE
                    clearAllNotiLog.visibility = View.INVISIBLE
                }else{
                    notiEmpty.visibility = View.INVISIBLE
                    clearAllNotiLog.visibility = View.VISIBLE
                }

            })

            deleteAllNotiLog.observe(viewLifecycleOwner, Observer {
                helper.viewNotiLog.deleteAllItems(requireActivity())
                //helper.viewClearItem.deleteAllItems(requireActivity())
            })

            deleteNotiLogByID.observe(viewLifecycleOwner,Observer{
                deleteNotiLogById(it)
            })

            goToNoti.observe(viewLifecycleOwner,Observer{
                helper.viewNotiLog.filterGoToMapBox(it,requireView())
            })

        }
    }


}