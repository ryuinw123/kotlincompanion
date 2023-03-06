package com.example.kmitlcompanion.ui.eventpage.helper

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.presentation.viewmodel.NotiLogPageViewModel
import com.example.kmitlcompanion.ui.eventpage.adaptor.NotiItemLogAdapter
import javax.inject.Inject

class ViewClearItem @Inject constructor(
) : DefaultLifecycleObserver {
//    @Inject lateinit var handler: Handler
//    private lateinit var runnable : Runnable
//
//    private lateinit var context : Context
//    private lateinit var viewModel : NotiLogPageViewModel
//    private lateinit var recyclerView : RecyclerView
//
//
//    fun setup(context: Context,viewModel: NotiLogPageViewModel,recyclerView: RecyclerView){
//        this.context = context
//        this.viewModel = viewModel
//        this.recyclerView = recyclerView
//
//    }
//
//    fun deleteAllItems(activity: Activity) {
//        var itemCount = notiItemLogAdapter.itemCount
//        runnable = Runnable {
//            Log.d("test_noti", itemCount.toString())
//            if (itemCount > 0){
//                itemCount -= 1
//                val view = recyclerView?.findViewHolderForAdapterPosition(itemCount)!!.itemView
//                deleteItem(view, itemCount )
//                handler.postDelayed(runnable, 400)
//            }else{
//                handler.removeCallbacks(runnable)
//            }
//        }
//        activity.runOnUiThread(runnable)
//    }
//
//    private fun deleteItem(rowView: View, position: Int) {
//        val anim: Animation = AnimationUtils.loadAnimation(
//            context,
//            R.anim.slide_out_left
//        )
//        anim.duration = 300
//        rowView.startAnimation(anim)
//        handler.postDelayed({
//            viewModel.removeItemByPosting(position)
//        }, anim.duration)
//    }

}