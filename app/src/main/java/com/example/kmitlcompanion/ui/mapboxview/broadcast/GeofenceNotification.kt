package com.example.kmitlcompanion.ui.mapboxview.broadcast

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.ui.mainactivity.MainActivity
import javax.inject.Inject

class GeofenceNotification @Inject constructor(
    private val context: Context
) {

    fun buildNotification(locationId: String): Notification {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "1", "channel_name", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        var locationName = "test"
        when (locationId) {
            "location_1" -> locationName = "Location 1"
            "location_2" -> locationName = "Location 2"
            "location_3" -> locationName = "Location 3"
            // Add more cases for additional locations
        }

        return NotificationCompat.Builder(context, "1")
            .setSmallIcon(R.drawable.ic_drawer)
            .setContentTitle("Welcome to $locationId")
            .setContentText("You are now inside the geofence area")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
    }
}