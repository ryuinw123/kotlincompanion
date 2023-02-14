package com.example.kmitlcompanion.ui.mapboxview.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.kmitlcompanion.databinding.TagItemBinding
import com.example.kmitlcompanion.domain.model.TagViewDataDetail
import javax.inject.Inject


class TagAdapter @Inject constructor(): ListAdapter<TagViewDataDetail,TagViewHolder>(ItemTag)
{
    private lateinit var onTagClickListener: OnTagClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val bd = TagItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagViewHolder(bd,onTagClickListener)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bindingTagViewHolder(getItem(position))
    }


    object ItemTag : DiffUtil.ItemCallback<TagViewDataDetail>() {
        override fun areItemsTheSame(oldItem: TagViewDataDetail, newItem: TagViewDataDetail): Boolean {
            //Log.d("test_DiffUtilsame",(oldItem.value.code == newItem.value.code).toString())
            return oldItem.value.code == newItem.value.code
        }

        override fun areContentsTheSame(oldItem: TagViewDataDetail, newItem: TagViewDataDetail): Boolean {
            //Log.d("test_DiffUtilcontentsame",(oldItem.status == newItem.status).toString())
            return oldItem.status == newItem.status
        }
    }

    fun setOnTagClickListener(listener: OnTagClickListener){
        onTagClickListener = listener
    }

    override fun submitList(list: MutableList<TagViewDataDetail>?) {
        super.submitList(list)
    }

}