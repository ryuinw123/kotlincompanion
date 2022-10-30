package com.example.kmitlcompanion.ui.mapboxview.helpers

import androidx.recyclerview.widget.RecyclerView
import com.example.kmitlcompanion.domain.model.Comment
import com.example.kmitlcompanion.presentation.MapboxViewModel
import com.example.kmitlcompanion.ui.mapboxview.adapter.CommentAdapter
import com.example.kmitlcompanion.ui.mapboxview.adapter.CommentClickListener
import javax.inject.Inject

class ViewComment @Inject constructor(
    private val commentAdapter: CommentAdapter
){
    fun setup(viewModel: MapboxViewModel , recyclerView: RecyclerView) {
        val commentClickListener = CommentClickListener { author, message, level, parentId ->
            val id = (viewModel.commentList.value?.size ?: 0) + 1
            viewModel.addComment(Comment(id,"15/05/2021 at 12:30", author, message, level, parentId = parentId))
        }
        commentAdapter.commentClickListener = commentClickListener

        recyclerView.adapter = commentAdapter
    }

    fun update(commentList : MutableList<Comment>) {
        commentAdapter.submitList(commentList)

    }

}