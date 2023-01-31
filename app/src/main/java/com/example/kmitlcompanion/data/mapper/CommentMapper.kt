package com.example.kmitlcompanion.data.mapper

import com.example.kmitlcompanion.data.model.CommentData
import com.example.kmitlcompanion.domain.model.Comment
import javax.inject.Inject

class CommentMapper @Inject constructor(

){
    fun mapToDomain(it: CommentData): Comment {
        return Comment(
            id = it.id,
            date= it.date,
            author= it.author,
            message= it.message,
            like= it.like,
            dislike = it.dislike,
            isLikedComment= it.isLikedComment,
            isDisLikedComment= it.isDisLikedComment,
            myComment= it.myComment
        )
    }
}