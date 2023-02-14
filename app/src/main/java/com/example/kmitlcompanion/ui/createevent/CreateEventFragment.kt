package com.example.kmitlcompanion.ui.createevent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import com.example.kmitlcompanion.ui.createlocation.CreateLocationFragmentArgs
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

        }

    }
    private fun FragmentCreateEventBinding.setupTextChange() {
        nameInput.doAfterTextChanged {
            viewModel?.updateNameInput(nameInput.text.toString())
        }
        detailInput.doAfterTextChanged {
            viewModel?.updateDetailInput(detailInput.text.toString())
        }
    }

}