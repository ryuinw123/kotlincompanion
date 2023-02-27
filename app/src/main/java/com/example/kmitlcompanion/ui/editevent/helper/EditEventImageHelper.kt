package com.example.kmitlcompanion.ui.editevent.helper

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.presentation.viewmodel.EditEventViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.ref.WeakReference
import javax.inject.Inject

class EditEventImageHelper @Inject constructor(){
    private var listOfWeakImageView : MutableList<WeakReference<ImageView?>?>? = null
    private var listOfWeakDiscardImageView : MutableList<WeakReference<FloatingActionButton?>?>? = null
    private lateinit var viewModel: EditEventViewModel
    private var currentUploadIndex : Int? = null

    private val handler = Handler(Looper.getMainLooper())

    fun setup(imageView: List<ImageView>, discardImageView: List<FloatingActionButton>, viewModel: EditEventViewModel){
        this.listOfWeakImageView = mutableListOf()
        this.listOfWeakDiscardImageView = mutableListOf()

        for (i in 0..4){
            this.listOfWeakImageView?.add(WeakReference(imageView[i]))
            this.listOfWeakDiscardImageView?.add(WeakReference(discardImageView[i]))
        }

        this.currentUploadIndex = 0
        this.viewModel = viewModel
    }

    fun setupStartImage(urlList: List<String>){
        val newList = urlList?.toMutableList()
        if (newList.toString() != "[]"){
            viewModel.setStartImage(urlList.toMutableList())
        }
    }

    fun loadingImage(urlList : MutableList<String>){
        if (urlList.toString() != "[]") {
            urlList.forEach {
                handler.post {
                    Picasso.get().load(it)
                        .into(object : Target {
                            override fun onBitmapLoaded(
                                bitmap: Bitmap?,
                                from: Picasso.LoadedFrom?
                            ) {
                                startImage(bitmap)
                            }

                            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                                startImage(null)
                                // handle the error on the main thread
                            }

                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                                // do something when the image is about to load on the main thread
                            }
                        })
                }
            }
        }

    }

    fun startImage(bitmap: Bitmap?) {
        //Log.d("currentUploadIndex",currentUploadIndex.toString())
        if (currentUploadIndex!! >= 5){
            currentUploadIndex = 4
        }

        //val bitmap = Picasso.get().load(url).get()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            //val source = ImageDecoder.createSource(context.contentResolver, uri)
            //val bitmap = ImageDecoder.decodeBitmap(source)
            listOfWeakImageView?.get(currentUploadIndex!!)?.get()?.setImageBitmap(bitmap)
        } else {
            //@Suppress("DEPRECATION") val bitmap =
            //    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
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

    fun updateImage(intent: Intent){
        viewModel.updateImage(intent)
    }

    fun setImage(uri: Uri, context : Context) {
        Log.d("currentUploadIndex",currentUploadIndex.toString())
        if (currentUploadIndex!! >= 5){
            currentUploadIndex = 4
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            val bitmap = ImageDecoder.decodeBitmap(source)
            listOfWeakImageView?.get(currentUploadIndex!!)?.get()?.setImageBitmap(bitmap)
        } else {
            @Suppress("DEPRECATION") val bitmap =
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
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

    fun clearImage() {

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