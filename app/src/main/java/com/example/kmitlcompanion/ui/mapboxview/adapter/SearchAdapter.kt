package com.example.kmitlcompanion.ui.mapboxview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.kmitlcompanion.databinding.EachItemBinding
import com.example.kmitlcompanion.databinding.TagItemBinding
import com.example.kmitlcompanion.domain.model.SearchDetail
import com.example.kmitlcompanion.generated.callback.OnClickListener
import com.example.kmitlcompanion.ui.createlocation.utils.TagTypeListUtil
import javax.inject.Inject

class SearchAdapter @Inject constructor(
    private val tagTypeListUtil: TagTypeListUtil,
): ListAdapter<SearchDetail,SearchViewHolder>(ItemSearch) {

    private lateinit var mOnSearchBackClickListener : OnEachSearchClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = EachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(view,mOnSearchBackClickListener,tagTypeListUtil)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    fun setOnEachClickListener(listener: OnEachSearchClickListener){
        mOnSearchBackClickListener = listener
    }

    object ItemSearch : DiffUtil.ItemCallback<SearchDetail>() {

        override fun areItemsTheSame(oldItem: SearchDetail, newItem: SearchDetail): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SearchDetail, newItem: SearchDetail): Boolean {
            return oldItem.name == newItem.name
        }
    }

}