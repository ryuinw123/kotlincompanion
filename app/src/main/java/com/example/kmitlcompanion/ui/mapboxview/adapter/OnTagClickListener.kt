package com.example.kmitlcompanion.ui.mapboxview.adapter

import com.example.kmitlcompanion.domain.model.TagViewDataDetail

interface OnTagClickListener {
    fun onTagClick(tagViewDataDetail: TagViewDataDetail,position: Int,code : Int)
}