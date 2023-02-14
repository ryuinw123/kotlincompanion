package com.example.kmitlcompanion.ui.mapboxview.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.EachItemBinding
import com.example.kmitlcompanion.domain.model.SearchDetail
import com.example.kmitlcompanion.ui.createlocation.utils.TagTypeListUtil
import javax.inject.Inject

class SearchViewHolder @Inject constructor(
    private val binding: EachItemBinding,
    private val onSearchBackClickListener: OnEachSearchClickListener,
    private val tagTypeListUtil: TagTypeListUtil,
) : RecyclerView.ViewHolder(binding.root){

    //@Inject var tagTypeListUtil : TagTypeListUtil

    private fun initSearchListItem(search: SearchDetail){
            binding.eachItemSearch.text = search.name
            binding.eachPicture.setImageResource(tagTypeListUtil.getImageByCode(search.code))
            binding.eachItemSearchDes.text = search.address

            binding.eachItemListSearch.setOnClickListener{
                onSearchBackClickListener.onItemClick(search,adapterPosition)
            }
    }

    fun bind(search: SearchDetail){
        initSearchListItem(search)
    }

}