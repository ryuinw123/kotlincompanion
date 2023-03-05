package com.example.kmitlcompanion.ui.mapboxview.broadcast

import android.content.Intent
import android.util.Log
import com.example.kmitlcompanion.domain.model.EventInformation
import com.example.kmitlcompanion.domain.model.NotiLogDetails
import com.example.kmitlcompanion.domain.usecases.SaveNotificationLogDetails
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.observers.DisposableObserver
import javax.inject.Inject

class SaveNotificationUtils @Inject constructor(
    private val saveNotificationLogDetails: SaveNotificationLogDetails
) {

    fun saveNotificationToCache(intent: Intent){
        saveNotificationLogDetails.execute(object : DisposableCompletableObserver(){
            override fun onComplete() {
                Log.d("test_noti_service","onComplete" + intent.toString())
            }

            override fun onError(e: Throwable) {
                Log.d("test_noti_service",e.toString())
            }
        }, params = NotiLogDetails(
            id = intent.getLongExtra("id",0),
            name = intent.getStringExtra("name") ?:"",
            startTime = intent.getStringExtra("startTime") ?:"",
            endTime = intent.getStringExtra("endTime") ?:"",
        ))
    }

}