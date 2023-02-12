package com.example.kmitlcompanion.ui.mapboxview.adapter

import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.TagItemBinding
import com.example.kmitlcompanion.domain.model.TagViewDataDetail

class TagViewHolder (
    private val binding: TagItemBinding,
    private val onTagClickListener: OnTagClickListener,
) : RecyclerView.ViewHolder(binding.root) {

    private fun initTagViewHolder(tagViewDataDetail: TagViewDataDetail){

        binding.tagName.text = tagViewDataDetail.value.name
        binding.imageIcon.setImageResource(tagViewDataDetail.value.icon)

        if (tagViewDataDetail.status){
            binding.mCardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context,R.color.kmitl_color))
            binding.imageIcon.setColorFilter(Color.WHITE)
        }else{
            binding.mCardView.setCardBackgroundColor(Color.WHITE)
            binding.imageIcon.setColorFilter(ContextCompat.getColor(itemView.context,R.color.kmitl_color))
        }

        binding.tagButton.setOnClickListener{
            onTagClickListener.onTagClick(tagViewDataDetail,adapterPosition,tagViewDataDetail.value.code)
        }
    }

    fun bindingTagViewHolder(tagViewDataDetail: TagViewDataDetail){
        initTagViewHolder(tagViewDataDetail)
    }

}