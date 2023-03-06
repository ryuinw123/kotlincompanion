package com.example.kmitlcompanion.ui.eventpage.adaptor

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.EventNotiLogItemBinding
import com.example.kmitlcompanion.domain.model.Comment
import com.example.kmitlcompanion.domain.model.NotiLogDetails
import com.example.kmitlcompanion.presentation.viewmodel.NotiLogPageViewModel
import javax.inject.Inject

class NotiItemLogAdapter(
    val viewModel: NotiLogPageViewModel
) : ListAdapter<NotiLogDetails, NotiItemLogViewHolder>(itemDiffUtil) {

    private lateinit var eventNotiLogItemBinding: EventNotiLogItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotiItemLogViewHolder {
        eventNotiLogItemBinding = EventNotiLogItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotiItemLogViewHolder(eventNotiLogItemBinding,parent.context)
    }

    override fun onBindViewHolder(holder: NotiItemLogViewHolder, position: Int) {
        holder.bind(getItem(position),viewModel)
    }

    object itemDiffUtil: DiffUtil.ItemCallback<NotiLogDetails>() {
        override fun areItemsTheSame(oldItem: NotiLogDetails, newItem: NotiLogDetails): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NotiLogDetails, newItem: NotiLogDetails): Boolean {
            return oldItem == newItem
        }
    }

}