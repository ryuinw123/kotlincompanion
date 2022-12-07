package com.example.kmitlcompanion.ui.createlocation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentCreatelocationBinding
import com.example.kmitlcompanion.databinding.FragmentCreatemapboxlocationBinding
import com.example.kmitlcompanion.presentation.viewmodel.CreateLocationViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.createlocation.helper.CreateLocationHelper
import com.example.kmitlcompanion.ui.createlocation.helper.ImageHelper
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateLocation : BaseFragment<FragmentCreatelocationBinding , CreateLocationViewModel>() {
    override val layoutId: Int = R.layout.fragment_createlocation

    override val viewModel: CreateLocationViewModel by viewModels()

    private val navArgs by navArgs<CreateLocationArgs>()

    private val requestGallery = 2121

    @Inject lateinit var helper: CreateLocationHelper

    override fun onReady(savedInstanceState: Bundle?) {
        viewModel.updateCurrentLocation(navArgs.currentLocation)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatelocationBinding.inflate(inflater,container,false).apply {
            viewModel = this@CreateLocation.viewModel
            helper.image.setup(selectImageView,this@CreateLocation.viewModel)
            val itemList = arrayListOf<String>("Hi","Mongol","SSS","Pokemon","One for all","All for One","hero","paps")
            helper.spinner.setupSpinnerAdaptor(typeSpinner,itemList,requireContext(),this@CreateLocation.viewModel)
            helper.upload.setup(this@CreateLocation.viewModel)
            setupViewObservers()
            setUpTextChange()
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
    private fun FragmentCreatelocationBinding.setupViewObservers() {
        this@CreateLocation.viewModel.run {
            currentLocation.observe(viewLifecycleOwner, Observer {
                this@setupViewObservers.nameInput.setText(it.place)
                this@setupViewObservers.detailInput.setText(it.address)
            })
            imageUpload.observe(viewLifecycleOwner, Observer {
                ImagePicker.with(this@CreateLocation).start(requestGallery)
            })
            imageData.observe(viewLifecycleOwner, Observer {
                val fileURI = imageData.value?.data
                helper.image.setImage(fileURI!!,requireContext())
            })

            publicUpload.observe(viewLifecycleOwner, Observer {
                helper.upload.uploadLocation()
            })

        }

    }
    private fun FragmentCreatelocationBinding.setUpTextChange() {
        nameInput.doAfterTextChanged {
            viewModel?.updateNameInput(nameInput.text.toString())
        }
        detailInput.doAfterTextChanged {
            viewModel?.updateDetailInput(detailInput.text.toString())
        }
    }
}