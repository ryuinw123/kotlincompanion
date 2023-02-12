package com.example.kmitlcompanion.ui.createlocation

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
import com.example.kmitlcompanion.databinding.FragmentCreateLocationBinding
import com.example.kmitlcompanion.presentation.viewmodel.CreateLocationViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.createlocation.helper.CreateLocationHelper
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateLocation : BaseFragment<FragmentCreateLocationBinding , CreateLocationViewModel>() {
    override val layoutId: Int = R.layout.fragment_create_location

    override val viewModel: CreateLocationViewModel by viewModels()

    private val navArgs by navArgs<CreateLocationArgs>()

    private val requestGallery = 2121

    @Inject lateinit var helper: CreateLocationHelper

    override fun onReady(savedInstanceState: Bundle?) {
        viewModel.updateCurrentLocation(navArgs.currentLocation)

        Log.d("createLocation","position = ${navArgs.currentLocation}")
        navArgs.currentLocation.polygon?.let {
            viewModel.polygonEvent()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateLocationBinding.inflate(inflater,container,false).apply {
            viewModel = this@CreateLocation.viewModel

            helper.image.setup(listOf(selectImageView1,selectImageView2,selectImageView3,selectImageView4,selectImageView5),
                                listOf(discardImage1,discardImage2,discardImage3,discardImage4,discardImage5),
                                this@CreateLocation.viewModel)

            val itemList = arrayListOf<String>(
                "ร้านอาหาร","ตึก","ร้านค้า","อีเวนท์","หอพัก"
            )
            helper.spinner.setup(typeSpinner,itemList,requireContext(),this@CreateLocation.viewModel)
            helper.upload.setup(this@CreateLocation.viewModel)
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

    private fun FragmentCreateLocationBinding.setupViewObservers() {
        this@CreateLocation.viewModel.run {
            currentLocation.observe(viewLifecycleOwner, Observer {
                this@setupViewObservers.nameInput.setText(it.place)
                this@setupViewObservers.detailInput.setText(it.address)
            })
            imageUpload.observe(viewLifecycleOwner, Observer {
                ImagePicker.with(this@CreateLocation).start(requestGallery)
            })
            discradImage.observe(viewLifecycleOwner, Observer {
                helper.image.clearImage(requireContext())
            })
            polygonEvent.observe(viewLifecycleOwner, Observer {
                helper.spinner.turnoffSpinner()
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
    private fun FragmentCreateLocationBinding.setupTextChange() {
        nameInput.doAfterTextChanged {
            viewModel?.updateNameInput(nameInput.text.toString())
        }
        detailInput.doAfterTextChanged {
            viewModel?.updateDetailInput(detailInput.text.toString())
        }
    }
}