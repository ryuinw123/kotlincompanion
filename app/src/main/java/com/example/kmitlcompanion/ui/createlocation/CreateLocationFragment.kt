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
import com.example.kmitlcompanion.ui.createlocation.helper.ImageHelper
import com.example.kmitlcompanion.ui.createlocation.utils.TagTypeListUtil
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateLocationFragment : BaseFragment<FragmentCreateLocationBinding , CreateLocationViewModel>() {
    override val layoutId: Int = R.layout.fragment_create_location

    override val viewModel: CreateLocationViewModel by viewModels()

    private val navArgs by navArgs<CreateLocationFragmentArgs>()

    private val requestGallery = 2121

    @Inject lateinit var helper: CreateLocationHelper
    @Inject lateinit var tagTypeListUtil : TagTypeListUtil

    override fun onReady(savedInstanceState: Bundle?) {
        viewModel.updateCurrentLocation(navArgs.currentLocation)

        Log.d("createLocation","position = ${navArgs.currentLocation}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateLocationBinding.inflate(inflater,container,false).apply {
            viewModel = this@CreateLocationFragment.viewModel

            helper.image.setup(listOf(selectImageView1,selectImageView2,selectImageView3,selectImageView4,selectImageView5),
                                listOf(discardImage1,discardImage2,discardImage3,discardImage4,discardImage5),
                                this@CreateLocationFragment.viewModel)

//            val itemList = arrayListOf<String>(
//                "ร้านอาหาร","ตึก","ร้านค้า","อีเวนท์","หอพัก"
//            )
            val itemList = tagTypeListUtil.getMutableListOfTagTypeString() as ArrayList<String>
            helper.spinner.setup(typeSpinner,itemList,requireContext(),this@CreateLocationFragment.viewModel)
            helper.upload.setup(this@CreateLocationFragment.viewModel)
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
        this@CreateLocationFragment.viewModel.run {
            currentLocation.observe(viewLifecycleOwner, Observer {
                this@setupViewObservers.nameInput.setText(it.place)
                this@setupViewObservers.detailInput.setText(it.address)
            })
            imageUpload.observe(viewLifecycleOwner, Observer {
                ImagePicker.with(this@CreateLocationFragment).start(requestGallery)
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
    private fun FragmentCreateLocationBinding.setupTextChange() {
        nameInput.doAfterTextChanged {
            viewModel?.updateNameInput(nameInput.text.toString())

            privateSubmit.isEnabled = (nameInput.text.toString().trim() != "")
            publicSubmit.isEnabled = (nameInput.text.toString().trim() != "")
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