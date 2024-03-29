package com.example.kmitlcompanion.ui.editevent.helper

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.kmitlcompanion.data.util.PicassoUtils
import com.example.kmitlcompanion.data.util.UnsafeOkHttpClientUtils
import com.example.kmitlcompanion.presentation.viewmodel.EditEventViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.ref.WeakReference
import javax.inject.Inject

class EditEventImageHelper @Inject constructor(
){
    private var listOfWeakImageView : MutableList<WeakReference<ImageView?>?>? = null
    private var listOfWeakDiscardImageView : MutableList<WeakReference<FloatingActionButton?>?>? = null
    private lateinit var viewModel: EditEventViewModel
    private var currentUploadIndex : Int? = null

    private val handler = Handler(Looper.getMainLooper())
    private val client = UnsafeOkHttpClientUtils.getUnsafeOkHttpClient()
    //private lateinit var picasso: Picasso
    private lateinit var context : Context
    //@Inject private lateinit var picassoUtils : PicassoUtils
    //private val picasso = PicassoUtils.getPicasso(context)

    fun setup(imageView: List<ImageView>, discardImageView: List<FloatingActionButton>, viewModel: EditEventViewModel,context: Context){
        this.listOfWeakImageView = mutableListOf()
        this.listOfWeakDiscardImageView = mutableListOf()

        for (i in 0..4){
            this.listOfWeakImageView?.add(WeakReference(imageView[i]))
            this.listOfWeakDiscardImageView?.add(WeakReference(discardImageView[i]))
        }

        this.currentUploadIndex = 0
        this.viewModel = viewModel
        this.context = context

    }

    fun setupStartImage(urlList: List<String>){
        val newList = urlList?.toMutableList()
        if (newList.toString() != "[]"){
            viewModel.setStartImage(urlList.toMutableList())
        }
    }

    fun loadingImage(urlList : MutableList<String>?){
        Log.d("test_image",urlList.toString())
        if (urlList.toString() != "[]"){
            urlList?.forEach {
                handler.post {
                    //Picasso.get().load(it)
                    PicassoUtils.getPicasso(context).load(it)
                        .into(object : Target {
                            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
//                                Log.d("test_image","onBitmapLoaded $it")
                                startImage(bitmap)
                            }

                            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
//                                Log.d("test_image","onBitmapFailed $it")
                                startImage(null)
                                // handle the error on the main thread
                            }

                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                                Log.d("test_image","onPrepareLoad $it")
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