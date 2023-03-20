package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.example.kmitlcompanion.ui.createevent.utils.EventTypeUtils
import java.lang.ref.WeakReference
import javax.inject.Inject

class ViewEvent @Inject constructor(
    private val eventTypeUtils: EventTypeUtils
) {

    private lateinit var viewModel: MapboxViewModel
    private lateinit var context: Context

    private lateinit var weakEventClickUrl : WeakReference<TextView>
    private lateinit var weakEventClickUrlValue : WeakReference<TextView>

    fun setup(viewModel: MapboxViewModel,context: Context,eventClickUrl : TextView,eventClickUrlValue: TextView){
        this.viewModel = viewModel
        this.context = context

        this.weakEventClickUrl = WeakReference(eventClickUrl)
        this.weakEventClickUrlValue = WeakReference(eventClickUrlValue)

    }

    fun setViewURLEnable(type : Int){
        eventClickUrl?.visibility = if (type == eventTypeUtils.getURLCode()) View.VISIBLE else View.GONE
        eventClickUrlValue?.visibility = if (type == eventTypeUtils.getURLCode()) View.VISIBLE else View.GONE
    }

    fun startIntentToBrowser(){
        val link = viewModel.eventUrl.value
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(intent)
    }


    private val eventClickUrl
        get() = weakEventClickUrl?.get()

    private val eventClickUrlValue
        get() = weakEventClickUrlValue?.get()

}