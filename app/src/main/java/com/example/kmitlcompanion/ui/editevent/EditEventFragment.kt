package com.example.kmitlcompanion.ui.editevent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import com.example.kmitlcompanion.databinding.FragmentEditEventBinding
import com.example.kmitlcompanion.presentation.viewmodel.EditEventViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.editevent.helper.EditEventHelper
import com.example.kmitlcompanion.ui.mainactivity.utils.BottomBarUtils
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditEventFragment : BaseFragment<FragmentEditEventBinding,EditEventViewModel>()  {

    @Inject lateinit var bottomBarUtils: BottomBarUtils
    @Inject lateinit var helper: EditEventHelper

    override val layoutId: Int = R.layout.fragment_edit_event

    override val viewModel: EditEventViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {
    }

    private val navArgs by navArgs<EditEventFragmentArgs>()

    private val requestGallery = 2121

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditEventBinding.inflate(inflater,container,false).apply {
            viewModel = this@EditEventFragment.viewModel

            helper.image.setup(
                listOf(selectImageView1,selectImageView2,selectImageView3,selectImageView4,selectImageView5),
                listOf(discardImage1,discardImage2,discardImage3,discardImage4,discardImage5),
                this@EditEventFragment.viewModel)
            helper.upload.setup(this@EditEventFragment.viewModel)

            //helper.datetime.setup(requireContext(), this@EditEventFragment.viewModel,startTimeTextInput,endTimeTextInput)

            helper.setup(this@EditEventFragment.viewModel)
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


    private fun FragmentEditEventBinding.setupViewObservers(){
        this@EditEventFragment.viewModel.run {
            editEventResponse.observe(viewLifecycleOwner, Observer {
                helper.updateData(navArgs.id,navArgs.name,navArgs.description,navArgs.startTime,navArgs.endTime)
                binding.nameInput.setText(navArgs.name)
                binding.detailInput.setText(navArgs.description)
                helper.image.setupStartImage(navArgs.imageLink?.toList() ?: listOf())

                binding.startTimeTextInput.isEnabled = false
                binding.endTimeTextInput.isEnabled = false
            })

            imageUpload.observe(viewLifecycleOwner, Observer {
                ImagePicker.with(this@EditEventFragment).start(requestGallery)
            })

            discardImage.observe(viewLifecycleOwner, Observer {
                helper.image.clearImage()
            })

            imageData.observe(viewLifecycleOwner, Observer {
                val fileURI = imageData.value?.data
                helper.image.setImage(fileURI!!,requireContext())
            })

            setStartImageUrl.observe(viewLifecycleOwner, Observer {
                helper.image.loadingImage(it)
            })

            startDateTimeValue.observe(viewLifecycleOwner, Observer {
                binding.startTimeTextInput.setText(it)
            })

            endDateTimeValue.observe(viewLifecycleOwner, Observer {
                binding.endTimeTextInput.setText(it)
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

    private fun FragmentEditEventBinding.setupTextChange() {
        nameInput.doAfterTextChanged {
            viewModel?.updateNameInput(nameInput.text.toString())
            submitButton.isEnabled = (nameInput.text.toString().trim() != "")
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