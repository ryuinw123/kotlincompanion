package com.example.kmitlcompanion.ui.mapboxview.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import com.example.kmitlcompanion.domain.model.EventTime
import com.example.kmitlcompanion.domain.usecases.GetLastestNotificationTime
import com.example.kmitlcompanion.domain.usecases.UpdateNotificationTime
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.observers.DisposableObserver
import java.sql.Timestamp
import javax.inject.Inject

@AndroidEntryPoint
class GeofenceReceiver : BroadcastReceiver() {

    @Inject lateinit var notificationUtils: NotificationUtils
    @Inject lateinit var getLastestNotificationTime : GetLastestNotificationTime
    @Inject lateinit var updateNotificationTime: UpdateNotificationTime
    @Inject lateinit var saveNotificationUtils: SaveNotificationUtils


    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.kmitl.geofence") {
            val id = intent.getLongExtra("id" , 0)
            val name = intent.getStringExtra("name")
            val type = intent.getIntExtra("type",0)
            val url = intent.getStringExtra("url")
            val thistime = Timestamp(System.currentTimeMillis())



            getLastestNotificationTime.execute(object : DisposableObserver<Timestamp>() {
                override fun onNext(t: Timestamp) {
                    dispose()
                    val lastesttime = t

                    var diffInMillis: Long = Math.abs(Timestamp(System.currentTimeMillis()).getTime() - lastesttime.getTime())
                    //val oneDayInMillis = (24 * 60 * 60 * 1000).toLong()
                    //val oneDayInMillis = (10 * 1000).toLong()
                    val oneDayInMillis = (60 * 10 * 1000).toLong()


                    //Log.d("test_noti","$diffInMillis and $oneDayInMillis")
                    Log.d("test_bug_poly",id.toString())

                    if (diffInMillis > oneDayInMillis) {
                        notificationUtils.sendGeofenceEnteredNotification(id , name ?: "Error",NotificationUtils.GEO_CHANNEL_ID)
                        saveNotificationUtils.saveNotificationToCache(intent)

                        if (type == 1){
                            notificationUtils.sendGeofenceEnteredNotificationWithUrl(id,NotificationUtils.GEO_CHANNEL_ID,url ?:"")
                        }

                        Log.d("Geofence" , "Transition In $id")

                        updateNotificationTime.execute(object : DisposableCompletableObserver() {
                            override fun onComplete() {
                                dispose()
                                //Log.d("test_noti","onComplete")
                            }

                            override fun onError(e: Throwable) {
                                //Log.d("test_noti",e.toString())
                            }

                        } , params = EventTime(
                            id = id.toInt(),
                            time = Timestamp(System.currentTimeMillis())
                        ))
                    }
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                }

            }, params = id.toInt())


        }
    }
}