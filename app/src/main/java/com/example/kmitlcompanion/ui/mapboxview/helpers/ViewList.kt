package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.marginEnd
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.ui.mapboxview.adapter.ImageAdapter
import com.example.kmitlcompanion.ui.mapboxview.utils.DpConverterUtils
import java.lang.Math.abs
import javax.inject.Inject


internal class ViewList @Inject constructor(
    private val imageAdapter: ImageAdapter,
    private val dpConverterUtils : DpConverterUtils
) {
    fun setupImageAdapter(viewPager2: ViewPager2 , imageList: MutableList<String>) {
        //if (!imageList[0].isNullOrEmpty()) {
        if (imageList.toString() != "[]") {
            imageAdapter.imageList = imageList
            imageAdapter.viewPager2 = viewPager2
            viewPager2.adapter = imageAdapter
            viewPager2.offscreenPageLimit = imageList.size
            viewPager2.clipToPadding = false
            viewPager2.clipChildren = false
            viewPager2.visibility = View.VISIBLE
            setUpTransformer(viewPager2)
        }else{
            viewPager2.visibility = View.GONE
        }

    }

    private fun setUpTransformer(viewPager2: ViewPager2) {
        val transformer = CompositePageTransformer()
        val pageMarginPx = dpConverterUtils.dpToPixel(20f)
        val offsetPx = dpConverterUtils.dpToPixel(30f)
        transformer.addTransformer { page,position ->
            val viewPager = page.parent.parent as ViewPager2
            val offset = position * -(2 * offsetPx + pageMarginPx)

            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f

            if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -offset
                } else {
                    page.translationX = offset
                }
            } else {
                page.translationY = offset
            }
        }

        viewPager2.setPageTransformer(transformer)
        //viewPager2.offscreenPageLimit = 1
    }
}