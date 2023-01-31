package com.example.kmitlcompanion.ui.mapboxview.adapter

import com.example.kmitlcompanion.domain.model.Comment

interface OnButtonClickListener {
    fun onButtonClick(comment: Comment,position: Int)
}