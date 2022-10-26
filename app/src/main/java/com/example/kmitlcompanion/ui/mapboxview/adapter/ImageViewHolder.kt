package com.example.kmitlcompanion.ui.mapboxview.adapter

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.kmitlcompanion.databinding.ImageViewBinding

internal class ImageViewHolder (
    private val binding: ImageViewBinding
        ) : RecyclerView.ViewHolder(binding.root){
            val imageView : ImageView = binding.imageView

}