package com.example.kmitlcompanion.ui.mapboxview.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.ChatCommentBinding
import com.example.kmitlcompanion.databinding.CommentMenuBinding
import com.example.kmitlcompanion.domain.model.Comment
import com.google.android.material.bottomsheet.BottomSheetDialog

class CommentViewHolder (
    private val binding: ChatCommentBinding,
    private val bindingMenu: CommentMenuBinding,
    private val commentListener: CommentClickListener,
    private val bottomSheetDialog: BottomSheetDialog,
    private val listenerLike: OnButtonClickListener,
    private val listenerDisLike: OnButtonClickListener,
    private val listenerMenu: OnMenuClickListener,
): RecyclerView.ViewHolder(binding.root) {

    private val listShowReplies = arrayListOf<Int>()

    private fun initBinding(binding: ChatCommentBinding, shortComment: Comment) {

        binding.btnLike.setOnClickListener {
            listenerLike.onButtonClick(shortComment,adapterPosition)
        }

        binding.btnDislike.setOnClickListener {
            listenerDisLike.onButtonClick(shortComment,adapterPosition)
        }

        binding.menu.visibility = View.GONE

        if(shortComment.myComment){
            binding.menu.visibility = View.VISIBLE

            binding.menu.setOnClickListener {
                bottomSheetDialog.setContentView(bindingMenu.root)
                listenerMenu.onMenuClick(shortComment,bottomSheetDialog)
            }

            bindingMenu.editLinearLayout.setOnClickListener{
                listenerMenu.onEditClick(shortComment,adapterPosition, bottomSheetDialog)
            }

            bindingMenu.deleteLinearLayout.setOnClickListener{
                listenerMenu.onDeleteClick(shortComment,adapterPosition, bottomSheetDialog)
            }
        }

//        bindingMenu.editLinearLayout.setOnClickListener{
//            listenerMenu.onEditClick(shortComment,bottomSheetDialog)
//        }
        when (shortComment.isLikedComment){
            true -> binding.btnLike.setIconResource(R.drawable.ic_like)
            false -> binding.btnLike.setIconResource(R.drawable.ic_like_unfill)
        }
        when (shortComment.isDisLikedComment){
            true -> binding.btnDislike.setIconResource(R.drawable.ic_dislike)
            false -> binding.btnDislike.setIconResource(R.drawable.ic_dislike_unfill)
        }

        binding.btnLike.text = shortComment.like.toString()
        binding.btnDislike.text = shortComment.dislike.toString()
        binding.tvComment.text = shortComment.message
        binding.tvDate.text = "${shortComment.date} - ${shortComment.author}"
    }

    fun bind(comment: Comment) {
        initBinding(binding, comment)
    }

}
