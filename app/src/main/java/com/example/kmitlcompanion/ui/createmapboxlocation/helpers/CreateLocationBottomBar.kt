package com.example.kmitlcompanion.ui.createmapboxlocation.helpers

import android.os.Handler
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.util.logging.LogRecord
import javax.inject.Inject

class CreateLocationBottomBar  @Inject constructor(

): DefaultLifecycleObserver
{
    @Inject lateinit var handler: Handler
    private lateinit var runnable : Runnable

    fun getLocationDetail(runnable: Runnable){
        this.runnable = runnable
        handler.removeCallbacks(this.runnable)
        handler.postDelayed(runnable,500)
    }


    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        handler.removeCallbacks(this.runnable)
    }
}