package com.example.kmitlcompanion.ui.mapboxview.adapter

open class CommentClickListener(
    private val sendComment: (author: String, message: String, level: Int, parentId: Int?) -> Unit
) {
    fun onSendComment(author: String, message: String, level: Int, parentId: Int? = null) =
        sendComment(author, message, level, parentId)
}