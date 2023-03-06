package com.example.kmitlcompanion.ui.createevent.helper

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.presentation.viewmodel.CreateEventViewModel
import com.example.kmitlcompanion.presentation.viewmodel.CreateLocationViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.ref.WeakReference
import javax.inject.Inject

class ImageHelper @Inject constructor(){
    private var listOfWeakImageView : MutableList<WeakReference<ImageView?>?>? = null
    private var listOfWeakDiscardImageView : MutableList<WeakReference<FloatingActionButton?>?>? = null
    private lateinit var viewModel: CreateEventViewModel
    private var currentUploadIndex : Int? = null


    fun setup(imageView: List<ImageView>, discardImageView: List<FloatingActionButton>, viewModel: CreateEventViewModel){
        this.listOfWeakImageView = mutableListOf()
        this.listOfWeakDiscardImageView = mutableListOf()

        for (i in 0..4){
            this.listOfWeakImageView?.add(WeakReference(imageView[i]))
            this.listOfWeakDiscardImageView?.add(WeakReference(discardImageView[i]))
        }

        Log.d("nulllcheck","helper")
        this.currentUploadIndex = 0
        this.viewModel = viewModel
    }

    fun updateImage(intent: Intent){
        viewModel.updateImage(intent)
    }

    fun setImage(uri: Uri,path : String?, context : Context) {
        Log.d("currentUploadIndex",currentUploadIndex.toString())
        if (currentUploadIndex!! >= 5){
            currentUploadIndex = 4
        }

        val options = BitmapFactory.Options().apply {
            inSampleSize = 12 // Set the sampling rate here
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            //val bitmap = ImageDecoder.decodeBitmap(source)
            val bitmap = BitmapFactory.decodeFile(path,options)
            listOfWeakImageView?.get(currentUploadIndex!!)?.get()?.setImageBitmap(bitmap)
        } else {
//            @Suppress("DEPRECATION") val bitmap =
//                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            val bitmap = BitmapFactory.decodeFile(path,options)
            listOfWeakImageView?.get(currentUploadIndex!!)?.get()?.setImageBitmap(bitmap)
        }


        for (i in 0..(currentUploadIndex!!-1)){
            listOfWeakDiscardImageView?.get(i)?.get()?.visibility = View.GONE
        }
        listOfWeakDiscardImageView?.get(currentUploadIndex!!)?.get()?.visibility = View.VISIBLE

        if (currentUploadIndex!! < 4){
            listOfWeakImageView?.get(currentUploadIndex!!)?.get()?.setOnClickListener(null)
            listOfWeakImageView?.get(currentUploadIndex!!+1)?.get()?.visibility = View.VISIBLE
        }
        currentUploadIndex = currentUploadIndex!! + 1

    }

    fun clearImage(context: Context) {

        if (currentUploadIndex!! > 0){
            currentUploadIndex = currentUploadIndex!! - 1
        }

        Log.d("currentUploadIndex",currentUploadIndex.toString())
        listOfWeakImageView?.get(currentUploadIndex!!)?.get()?.setImageResource(R.drawable.upload_file_fill1_wght400_grad200_opsz48)
        listOfWeakDiscardImageView?.get(currentUploadIndex!!)?.get()?.visibility = View.GONE
        if (currentUploadIndex!! != 0) {
            listOfWeakDiscardImageView?.get(currentUploadIndex!! - 1)?.get()?.visibility = View.VISIBLE
        }
        if (currentUploadIndex!! < 4 ){
            listOfWeakImageView?.get(currentUploadIndex!!)?.get()?.setOnClickListener(View.OnClickListener { viewModel.uploadImage()})
            listOfWeakImageView?.get(currentUploadIndex!!+1)?.get()?.visibility = View.GONE
        }

        viewModel.removeImage()

    }
}