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
        if(shortComment.replies.isNotEmpty() && shortComment.level == 1)
            createNestedComment(binding, shortComment)
        binding.btnReply.setOnClickListener {
            val dialogCommentView = LayoutInflater.from(it.context).inflate(R.layout.dialog_comment, null, false)
            MaterialAlertDialogBuilder(it.context)
                .setTitle("Post a comment")
                .setView(dialogCommentView)
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Send") { _,_ ->
                    val author: String = dialogCommentView.findViewById<EditText>(R.id.ti_author).text.toString()
                    val message: String = dialogCommentView.findViewById<EditText>(R.id.ti_message).text.toString()
                    commentListener.onSendComment(
                        author,
                        message,
                        shortComment.level + 1,
                        shortComment.parentId ?: shortComment.id
                    )
                }
                .show()
        }
    }

    fun bind(comment: Comment) {
        initBinding(binding, comment)
    }

    private fun createNestedComment(binding: ChatCommentBinding, comment: Comment) {
        binding.btnMore.isVisible = true
        binding.btnMore.text = "Show ${comment.replies.size} replies"
        binding.btnMore.setOnClickListener {
            if(!listShowReplies.contains(comment.id)) {
                listShowReplies.add(comment.id)
                binding.btnMore.text = "Hide replies"
                comment.replies.forEach { nestedComment ->
                    val newComment = ChatCommentBinding.inflate(LayoutInflater.from(binding.root.context), null, false)
                    initBinding(newComment, nestedComment)
                    binding.llReplies.addView(newComment.root)
                }
                binding.llReplies.isVisible = true
            } else {
                binding.btnMore.text = "Show ${comment.replies.size} replies"
                listShowReplies.remove(comment.id)
                binding.llReplies.removeAllViews()
                binding.llReplies.isVisible = false
            }
        }
    }
}
