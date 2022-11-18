package com.example.kmitlcompanion.ui.mapboxview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.ChatCommentBinding
import com.example.kmitlcompanion.domain.model.Comment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject

class CommentAdapter @Inject constructor()
    : ListAdapter<Comment, CommentViewHolder>(ShortCommentDiffUtil) {
    lateinit var commentClickListener: CommentClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(ChatCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false),commentClickListener)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    object ShortCommentDiffUtil: DiffUtil.ItemCallback<Comment>() {

        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }
    }

}