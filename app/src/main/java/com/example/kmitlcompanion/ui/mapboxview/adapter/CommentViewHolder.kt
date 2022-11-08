package com.example.kmitlcompanion.ui.mapboxview.adapter

import android.view.LayoutInflater
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.ChatCommentBinding
import com.example.kmitlcompanion.domain.model.Comment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CommentViewHolder (private val binding: ChatCommentBinding , private val commentListener: CommentClickListener): RecyclerView.ViewHolder(binding.root) {

    private val listShowReplies = arrayListOf<Int>()

    private fun initBinding(binding: ChatCommentBinding, shortComment: Comment) {
        binding.tvComment.text = shortComment.message
        binding.tvDate.text = "${shortComment.date} - ${shortComment.author}"
    }

    fun bind(comment: Comment) {
        initBinding(binding, comment)
    }

}
