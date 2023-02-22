package com.example.kmitlcompanion.ui.mapboxview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.kmitlcompanion.databinding.ChatCommentBinding
import com.example.kmitlcompanion.databinding.CommentMenuBinding
import com.example.kmitlcompanion.domain.model.Comment
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class CommentAdapter @Inject constructor()
    : ListAdapter<Comment, CommentViewHolder>(ShortCommentDiffUtil) {
    lateinit var commentClickListener: CommentClickListener

    private lateinit var likeListener: OnButtonClickListener
    private lateinit var disLikeListener: OnButtonClickListener
    private lateinit var menuListener: OnMenuClickListener
    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {

        //bottomSheetDialog = BottomSheetDialog(parent.context)
        //bottomSheetDialog.setContentView(CommentMenuBinding.inflate(LayoutInflater.from(parent.context)).root)

        return CommentViewHolder(
            ChatCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            CommentMenuBinding.inflate(LayoutInflater.from(parent.context)),
            commentClickListener,
            BottomSheetDialog(parent.context),
            likeListener,
            disLikeListener,
            menuListener
        )

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

    fun setOnLikeClickListener(listener: OnButtonClickListener){
        likeListener = listener
    }

    fun setOnDisLikeClickListener(listener: OnButtonClickListener){
        disLikeListener = listener
    }

    fun setOnMenuClickListener(listener: OnMenuClickListener){
        menuListener = listener
    }

}