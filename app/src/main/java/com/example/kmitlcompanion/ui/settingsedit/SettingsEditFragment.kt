package com.example.kmitlcompanion.ui.settingsedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentSettingsEditBinding
import com.example.kmitlcompanion.presentation.viewmodel.SettingsEditViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.mainactivity.utils.BottomBarUtils
import com.example.kmitlcompanion.ui.settingsedit.helper.SettingsEditHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsEditFragment : BaseFragment<FragmentSettingsEditBinding,SettingsEditViewModel>() {

    override val layoutId: Int = R.layout.fragment_settings_edit

    override val viewModel: SettingsEditViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {
    }

    @Inject internal lateinit var helper: SettingsEditHelper

    @Inject lateinit var bottomBarUtils: BottomBarUtils

    private val navArgs by navArgs<SettingsEditFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSettingsEditBinding.inflate(inflater,container,false).apply {
            viewModel = this@SettingsEditFragment.viewModel

            helper.setup(
                this@SettingsEditFragment.viewModel,
                requireContext(),
                textSettingsHeader,
                textBar,
                spiner,
                textInput,
                spinHead,
                editSpinner,
                navArgs
            )
            helper.spinner.setup(
                editSpinner,navArgs,
                requireContext(),
                this@SettingsEditFragment.viewModel
            )
            setupViewObservers()
            setupTextChange()
        }

        bottomBarUtils?.bottomMap?.visibility = View.INVISIBLE
        return binding.root
    }

    private fun FragmentSettingsEditBinding.setupViewObservers(){
        this@SettingsEditFragment.viewModel.run {

            editState.observe(viewLifecycleOwner , Observer {
                when (it){
                    0 -> spiner.visibility = View.GONE
                    1 -> spiner.visibility = View.GONE
                    2 -> textBar.visibility = View.GONE
                    3 -> textBar.visibility = View.GONE
                    4 -> textBar.visibility = View.GONE
                }
            })

            updateSpinner.observe(viewLifecycleOwner, Observer {
                helper.spinner.setSpinner(it)
            })

            spinnerTrigger.observe(viewLifecycleOwner, Observer {
                helper.updateSpinnerByState(it)
            })

        }
    }

    private fun FragmentSettingsEditBinding.setupTextChange(){
        textInput.doAfterTextChanged {
            viewModel?.updateUserNameInput(textInput.text.toString())
            submitButton.isEnabled = (textInput.text.toString().trim() != "")
        }
    }


}