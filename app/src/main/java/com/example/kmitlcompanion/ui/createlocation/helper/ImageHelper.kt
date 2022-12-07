package com.example.kmitlcompanion.ui.createlocation.helper

import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import com.example.kmitlcompanion.presentation.viewmodel.CreateLocationViewModel
import java.lang.ref.WeakReference
import javax.inject.Inject

class ImageHelper @Inject constructor(){
    private var weakImageView : WeakReference<ImageView?>? = null
    private lateinit var viewModel: CreateLocationViewModel

    fun setup(imageView: ImageView , viewModel: CreateLocationViewModel){
        this.weakImageView = WeakReference(imageView)
        this.viewModel = viewModel
    }

    fun updateImage(intent: Intent){
        viewModel.updateImage(intent)
    }

    fun setImage(uri: Uri, context : Context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            val bitmap = ImageDecoder.decodeBitmap(source)
            weakImageView?.get()?.setImageBitmap(bitmap)

        } else {
            @Suppress("DEPRECATION") val bitmap =
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            weakImageView?.get()?.setImageBitmap(bitmap)
        }
    }
}