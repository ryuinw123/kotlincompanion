package com.example.kmitlcompanion.ui.eventpage.adaptor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.kmitlcompanion.data.util.PicassoUtils
import com.example.kmitlcompanion.databinding.EventNotiLogItemBinding
import com.example.kmitlcompanion.databinding.FragmentNotiLogPageBinding
import com.example.kmitlcompanion.domain.model.Comment
import com.example.kmitlcompanion.domain.model.NotiLogDetails
import com.example.kmitlcompanion.presentation.viewmodel.NotiLogPageViewModel
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class NotiItemLogViewHolder(
    val binding: EventNotiLogItemBinding,
    val context: Context
): RecyclerView.ViewHolder(binding.root) {

    private val timeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    private fun initBinding(binding: EventNotiLogItemBinding,notiLogDetails: NotiLogDetails){

        binding.notiLogName.text = notiLogDetails.name

        val nowTime = Calendar.getInstance().time
        val endTime = timeFormat.parse(notiLogDetails.endTime)

        var statusText = ""
        if(endTime > nowTime){
            binding.alive = true
            statusText = "อีเวนต์กำลังจัดอยู่"
        }else{
            binding.alive = false
            statusText = "อีเวนต์จบลงแล้ว"
        }

        binding.id = notiLogDetails.id
        binding.notiLogStatus.text = statusText

        if (notiLogDetails.imageLinks != ""){
            PicassoUtils.getPicasso(context).load(notiLogDetails.imageLinks).into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    binding.itemNotiPicture.setImageBitmap(bitmap)
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    binding.itemNotiPicture.setImageBitmap(null)
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }
            })
        }

    }

    fun bind(notiLogDetails: NotiLogDetails,viewModel: NotiLogPageViewModel) {
        binding.viewModel = viewModel
        initBinding(binding, notiLogDetails)
        //binding.executePendingBindings()
    }

}