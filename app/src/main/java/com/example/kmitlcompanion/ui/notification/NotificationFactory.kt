package com.example.kmitlcompanion.ui.notification

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.contentValuesOf
import com.example.kmitlcompanion.R
import javax.inject.Inject

class NotificationFactory {

    companion object {

        fun createNotification(context : Context): NotificationCompat.Builder {
            return NotificationCompat.Builder(context, "YOUR_NOTIFICATION_CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_drawer)
                .setContentTitle("Location Service")
                .setContentText("Getting location in the background")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        }
    }
}