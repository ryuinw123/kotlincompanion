package com.example.kmitlcompanion.ui.mapboxview.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.data.util.PicassoUtils
import com.example.kmitlcompanion.data.util.UnsafeOkHttpClientUtils
import com.mapbox.maps.extension.style.expressions.dsl.generated.image
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.internal.notify
import javax.inject.Inject

internal class ImageAdapter @Inject constructor(
    ) : RecyclerView.Adapter<ImageViewHolder>() {

    lateinit var imageList : MutableList<String>
    lateinit var viewPager2: ViewPager2
    private val client = UnsafeOkHttpClientUtils.getUnsafeOkHttpClient()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.image_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val context = holder.imageView.context
        PicassoUtils.getPicasso(context).load(imageList[position]).into(holder.imageView)
        if (position == imageList.size-1) {
            viewPager2.post(runnable)
        }

    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    private val runnable = Runnable {
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }

}