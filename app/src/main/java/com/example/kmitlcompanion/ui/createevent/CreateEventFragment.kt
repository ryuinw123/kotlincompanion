package com.example.kmitlcompanion.ui.createevent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentCreateEventBinding
import com.example.kmitlcompanion.presentation.viewmodel.CreateEventViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.createevent.helper.CreateEventHelper
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateEventFragment : BaseFragment<FragmentCreateEventBinding , CreateEventViewModel>() {

    override val layoutId: Int = R.layout.fragment_create_event

    override val viewModel: CreateEventViewModel by viewModels()

    private val requestGallery = 2121


    private val navArgs by navArgs<CreateEventFragmentArgs>()

    @Inject lateinit var helper: CreateEventHelper

    override fun onReady(savedInstanceState: Bundle?) {
        viewModel.updateCurrentLocation(navArgs.event)
        Log.d("createLocation","position = ${navArgs.event}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateEventBinding.inflate(inflater,container,false).apply {

            viewModel = this@CreateEventFragment.viewModel

            helper.image.setup(listOf(selectImageView1,selectImageView2,selectImageView3,selectImageView4,selectImageView5),
                listOf(discardImage1,discardImage2,discardImage3,discardImage4,discardImage5),
                this@CreateEventFragment.viewModel)

            helper.upload.setup(this@CreateEventFragment.viewModel)

            helper.datetime.setup(requireContext(), this@CreateEventFragment.viewModel,startTimeTextInput,endTimeTextInput)
            setupViewObservers()
            setupTextChange()

        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == requestGallery && data != null) {
                helper.image.updateImage(data)
                //ImagePicker.getFile(data)?.let { doRequest(it,fileUri) } ?: run{ Toast.makeText(applicationContext,"Ops. Something went wrong.",Toast.LENGTH_SHORT)}
            }
        }
    }

    private fun FragmentCreateEventBinding.setupViewObservers() {
        this@CreateEventFragment.viewModel.run {
            imageUpload.observe(viewLifecycleOwner, Observer {
                ImagePicker.with(this@CreateEventFragment).start(requestGallery)
            })
            discardImage.observe(viewLifecycleOwner, Observer {
                helper.image.clearImage(requireContext())
            })

            imageData.observe(viewLifecycleOwner, Observer {
                //Log.d("nulllcheck",imageData.value?.isNotEmpty().toString())
                Log.d("nulllcheck","fragment")
                //if (imageData.value!!.isNotEmpty()){
                //val fileURI = imageData.value?.last()?.data
                val fileURI = imageData.value?.data
                helper.image.setImage(fileURI!!,requireContext())
                //}
            })

            publicUpload.observe(viewLifecycleOwner, Observer {
                helper.upload.uploadLocation(public = true)
            })

            privateUpload.observe(viewLifecycleOwner, Observer {
                helper.upload.uploadLocation(public = false)
            })

            //For datetime
            startDateTimePickEvent.observe(viewLifecycleOwner, Observer {
                helper.datetime.openDateTimePicker()
            })

            endDateTimePickEvent.observe(viewLifecycleOwner, Observer {
                helper.datetime.openEndDateTimePicker()
            })

            startDateTimeValue.observe(viewLifecycleOwner, Observer {
                //submitButton.isEnabled = endDateTimeValue.value?.timeInMillis!! - startDateTimeValue.value?.timeInMillis!! >= 10 * 60 * 1000
                submitButton.isEnabled = nameInput != null && !TextUtils.isEmpty(binding.nameInput.text?.trim()) && endDateTimeValue.value?.timeInMillis!! - startDateTimeValue.value?.timeInMillis!! >= 10 * 60 * 1000
            })

            endDateTimeValue.observe(viewLifecycleOwner, Observer {
                //submitButton.isEnabled = endDateTimeValue.value?.timeInMillis!! - startDateTimeValue.value?.timeInMillis!! >= 10 * 60 * 1000
                submitButton.isEnabled = nameInput != null && !TextUtils.isEmpty(binding.nameInput.text?.trim()) && endDateTimeValue.value?.timeInMillis!! - startDateTimeValue.value?.timeInMillis!! >= 10 * 60 * 1000

            })

            uploadLoading.observe(viewLifecycleOwner, Observer {
                if (it == true){
                    binding.allScreen.alpha = 0.5f
                    val scrollView = binding.allScreen
                    disableAllClicks(scrollView)
                    uploadLoading.value = false
                }
            })

        }

    }
    private fun FragmentCreateEventBinding.setupTextChange() {
        nameInput.doAfterTextChanged {
            viewModel?.updateNameInput(nameInput.text.toString())
            submitButton.isEnabled = nameInput != null && !TextUtils.isEmpty(nameInput.text?.trim()) && viewModel?.endDateTimeValue?.value?.timeInMillis!! - viewModel?.startDateTimeValue?.value?.timeInMillis!! >= 10 * 60 * 1000
        }
        detailInput.doAfterTextChanged {
            viewModel?.updateDetailInput(detailInput.text.toString())
        }
    }

    private fun disableAllClicks(view: View) {
        view.isEnabled = false
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                disableAllClicks(child)
            }
        }
    }
}