package com.example.kmitlcompanion.ui.mapboxview.adapter

import com.example.kmitlcompanion.domain.model.SearchDetail

interface OnEachSearchClickListener {
    fun onItemClick(searchDetail: SearchDetail,position: Int)
}