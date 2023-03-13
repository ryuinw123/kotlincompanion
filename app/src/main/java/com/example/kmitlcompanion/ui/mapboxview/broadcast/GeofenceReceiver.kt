package com.example.kmitlcompanion.ui.mapboxview.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.kmitlcompanion.domain.model.EventTime
import com.example.kmitlcompanion.domain.usecases.GetLastestNotificationTime
import com.example.kmitlcompanion.domain.usecases.UpdateNotificationTime
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.observers.DisposableObserver
import java.sql.Time
import java.sql.Timestamp
import javax.inject.Inject

@AndroidEntryPoint
class GeofenceReceiver : BroadcastReceiver() {

    @Inject lateinit var notificationUtils: NotificationUtils
    @Inject lateinit var getLastestNotificationTime : GetLastestNotificationTime
    @Inject lateinit var updateNotificationTime: UpdateNotificationTime

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.kmitl.geofence") {
            val id = intent.getLongExtra("id" , 0)
            val name = intent.getStringExtra("name")
            val thistime = Timestamp(System.currentTimeMillis())

            getLastestNotificationTime.execute(object : DisposableObserver<Timestamp>() {
                override fun onNext(t: Timestamp) {
                    val lastesttime = t

                    val diffInMillis: Long = Math.abs(thistime.getTime() - lastesttime.getTime())
                    val oneDayInMillis = (24 * 60 * 60 * 1000).toLong()

                    if (diffInMillis > oneDayInMillis) {
                        notificationUtils.sendGeofenceEnteredNotification(id , name ?: "Error",NotificationUtils.GEO_CHANNEL_ID)
                        Log.d("Geofence" , "Transition In")

                        updateNotificationTime.execute(object : DisposableCompletableObserver() {
                            override fun onComplete() {
                            }

                            override fun onError(e: Throwable) {
                            }

                        } , params = EventTime(
                            id = id.toInt(),
                            time = thistime
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