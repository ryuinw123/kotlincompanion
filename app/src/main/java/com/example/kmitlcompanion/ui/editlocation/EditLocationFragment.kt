package com.example.kmitlcompanion.ui.editlocation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentEditLocationBinding
import com.example.kmitlcompanion.presentation.viewmodel.EditLocationViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.createlocation.utils.TagTypeListUtil
import com.example.kmitlcompanion.ui.editlocation.helper.EditLocationHelper
import com.example.kmitlcompanion.ui.mainactivity.utils.BottomBarUtils
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditLocationFragment : BaseFragment<FragmentEditLocationBinding,EditLocationViewModel>()  {

    @Inject lateinit var bottomBarUtils: BottomBarUtils
    @Inject lateinit var helper: EditLocationHelper
    @Inject lateinit var tagTypeListUtil : TagTypeListUtil


    override val layoutId: Int = R.layout.fragment_edit_location

    override val viewModel: EditLocationViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {
    }

    private val navArgs by navArgs<EditLocationFragmentArgs>()

    private val requestGallery = 2121



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditLocationBinding.inflate(inflater,container,false).apply {
            viewModel = this@EditLocationFragment.viewModel

            helper.image.setup(
                listOf(selectImageView1,selectImageView2,selectImageView3,selectImageView4,selectImageView5),
                listOf(discardImage1,discardImage2,discardImage3,discardImage4,discardImage5),
                this@EditLocationFragment.viewModel)

            val itemList = tagTypeListUtil.getMutableListOfTagTypeString() as ArrayList<String>
            helper.spinner.setup(typeSpinner,itemList,requireContext(),this@EditLocationFragment.viewModel)
            helper.upload.setup(this@EditLocationFragment.viewModel)

            helper.setup(this@EditLocationFragment.viewModel)

            setupViewObservers()
            setupTextChange()
        }


        Log.d("test_edit",navArgs.toString())

        bottomBarUtils.bottomMap?.visibility = View.INVISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == requestGallery && data != null) {
                Log.d("test_edit",data.toString())

                helper.image.updateImage(data)

            }
        }
    }

    private fun FragmentEditLocationBinding.setupViewObservers(){
        this@EditLocationFragment.viewModel.run {
            editLocationResponse.observe(viewLifecycleOwner, Observer {
                helper.updateData(navArgs.id,navArgs.name,navArgs.description,navArgs.type)
                binding.nameInput.setText(navArgs.name)
                binding.detailInput.setText(navArgs.description)
                helper.spinner.setSpinner(navArgs.type ?:"")
                helper.image.setupStartImage(navArgs.imageLink?.toList())
            })

            imageUpload.observe(viewLifecycleOwner, Observer {
                ImagePicker.with(this@EditLocationFragment).start(requestGallery)
            })
            discardImage.observe(viewLifecycleOwner, Observer {
                helper.image.clearImage()
            })

            imageData.observe(viewLifecycleOwner, Observer {
                Log.d("nulllcheck","fragment")
                val fileURI = imageData.value?.data
                helper.image.setImage(fileURI!!,requireContext())
            })

            setStartImageUrl.observe(viewLifecycleOwner, Observer {
                helper.image.loadingImage(it)
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

    private fun FragmentEditLocationBinding.setupTextChange() {
        nameInput.doAfterTextChanged {
            viewModel?.updateNameInput(nameInput.text.toString())
            submitEditLocation.isEnabled = (nameInput.text.toString().trim() != "")
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