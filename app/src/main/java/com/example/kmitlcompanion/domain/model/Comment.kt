package com.example.kmitlcompanion.domain.model

data class Comment(
    val id: Int,
    val date: String,
    val author: String,
    val message: String,
    val level: Int = 1,
    val replies: MutableList<Comment> = mutableListOf(),
    val parentId: Int? = null
)
