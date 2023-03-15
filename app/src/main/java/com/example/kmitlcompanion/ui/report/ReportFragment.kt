package com.example.kmitlcompanion.ui.report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentCreateLocationBinding
import com.example.kmitlcompanion.databinding.FragmentReportBinding
import com.example.kmitlcompanion.presentation.viewmodel.ReportViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.mainactivity.utils.BottomBarUtils
import com.example.kmitlcompanion.ui.report.helper.ReportHelper
import com.example.kmitlcompanion.ui.report.utils.ReportReasonUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReportFragment : BaseFragment<FragmentReportBinding,ReportViewModel>() {

    @Inject
    internal lateinit var helper: ReportHelper
    @Inject
    lateinit var bottomBarUtils: BottomBarUtils
    @Inject
    lateinit var reportReasonUtils: ReportReasonUtils

    override val layoutId: Int = R.layout.fragment_report

    override val viewModel: ReportViewModel by viewModels()

    private val navArgs by navArgs<ReportFragmentArgs>()

    override fun onReady(savedInstanceState: Bundle?) {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportBinding.inflate(inflater, container, false).apply {
            viewModel = this@ReportFragment.viewModel
            setupViewObservers()
            setupTextChange()

            helper.viewReport.setup(this@ReportFragment.viewModel, requireContext(),
                navArgs.type,
                navArgs.id,
                navArgs.name ?:"",
                reportHeader,
                nameReport,
            )
            helper.spinnerReportHelper.setup(reportSpinner, reportReasonUtils.getReason(navArgs.type),requireContext(),this@ReportFragment.viewModel)
        }

        bottomBarUtils.bottomMap?.visibility = View.INVISIBLE
        return binding.root
    }

    private fun FragmentReportBinding.setupViewObservers() {
        this@ReportFragment.viewModel.run {
            submitReportTrigger.observe(viewLifecycleOwner , Observer {
                if (eventState.value?.peekContent() == true){
                    sendReportToEvent()
                }else{
                    sendReportToMarker()
                }
            })

            spinnerType.observe(viewLifecycleOwner, Observer {
                reportSubmitButton.isEnabled = !(viewModel?.spinnerType?.value == reportReasonUtils.getReason(navArgs.type).last() && detailReportInput.text.toString().replace(" ","") == "")
            })
        }
    }

    private fun FragmentReportBinding.setupTextChange() {
        detailReportInput.doAfterTextChanged {
            reportSubmitButton.isEnabled = !(viewModel?.spinnerType?.value == reportReasonUtils.getReason(navArgs.type).last() && detailReportInput.text.toString().replace(" ","") == "")
            viewModel?.updateDetails(detailReportInput.text.toString().replace(" ",""))
        }
    }


}