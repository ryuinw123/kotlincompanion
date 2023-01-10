package com.example.kmitlcompanion.ui.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.mapbox.maps.extension.style.expressions.dsl.generated.not
import okhttp3.internal.notify
import javax.inject.Inject


const val channel_ID = "com.example.notificationapp"

class NotificationManagerCalling @Inject constructor(
    private val context: Context,
    private val notification: Notification
    ) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    lateinit var notificationChannel: NotificationChannel

    fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channel_ID,"notification",NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

    fun startNotify() {
        notificationManager.notify(0, notification)
    }
}