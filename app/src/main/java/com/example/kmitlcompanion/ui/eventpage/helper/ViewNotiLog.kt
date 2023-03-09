package com.example.kmitlcompanion.ui.eventpage.helper

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast.makeText
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.domain.model.NotiLogDetails
import com.example.kmitlcompanion.presentation.viewmodel.NotiLogPageViewModel
import com.example.kmitlcompanion.ui.eventpage.adaptor.NotiItemLogAdapter
import com.example.kmitlcompanion.ui.mapboxview.utils.ToasterUtil
import com.google.android.material.snackbar.Snackbar
import java.lang.ref.WeakReference
import javax.inject.Inject


class ViewNotiLog @Inject constructor(
    private val toasterUtil: ToasterUtil,
) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT
) {

    private lateinit var viewModel: NotiLogPageViewModel
    private lateinit var context: Context
    private lateinit var weakRecyclerView : WeakReference<RecyclerView>
    private lateinit var itemTouchHelper : ItemTouchHelper
    private lateinit var notiItemLogAdapter : NotiItemLogAdapter

    @Inject lateinit var handler: Handler
    private lateinit var runnable : Runnable

    fun setup(viewModel: NotiLogPageViewModel,context: Context,recyclerView: RecyclerView){
        this.viewModel = viewModel
        this.context = context
        this.weakRecyclerView = WeakReference(recyclerView)

        viewModel.downloadNotiLog()

        notiItemLogAdapter = NotiItemLogAdapter(viewModel)

        // Attach ItemTouchHelper to RecyclerView
        itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = notiItemLogAdapter
    }

    fun update(notiLogDetailsList: MutableList<NotiLogDetails>) {
        notiItemLogAdapter.submitList(notiLogDetailsList)
        notiItemLogAdapter.notifyDataSetChanged()
    }

    fun deleteAllItems(activity: Activity) {
        var itemCount = notiItemLogAdapter.itemCount
        runnable = Runnable {
            if (itemCount > 0){
                itemCount -= 1
                val view = recyclerView?.findViewHolderForAdapterPosition(itemCount)!!.itemView
                deleteItem(view, itemCount )
                handler.postDelayed(runnable, 300)
            }else{
                handler.removeCallbacks(runnable)
            }
        }
        activity.runOnUiThread(runnable)
    }

    private fun deleteItem(rowView: View, position: Int) {
        val anim: Animation = AnimationUtils.loadAnimation(
            context,
            R.anim.slide_out_left
        )
        anim.duration = 250
        rowView.startAnimation(anim)
        handler.postDelayed({
            viewModel.removeItemByPosting(position)
        }, anim.duration)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //removeItem(viewHolder.adapterPosition)
        viewModel.removeItem(position = viewHolder.adapterPosition)
        //Log.d("test_noti",viewModel.notiLogData.value.toString())
        //viewModel.deleteNotiLogById(notiItemLogAdapter.currentList[viewHolder.adapterPosition].id)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }


    fun filterGoToMapBox(pair: Pair<Long,Boolean>,view: View){
        if (pair.second){
            viewModel.goToMapBox(pair.first)
        }else{
            toasterUtil.showToast("อีเวนต์นี้จบลงแล้ว")
            //Snackbar.make(view,"อีเวนต์นี้จบลงแล้ว", Snackbar.LENGTH_SHORT).show()
        }
    }

    private val recyclerView
        get() = weakRecyclerView.get()

}