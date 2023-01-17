package com.example.kmitlcompanion.ui.mapboxview.broadcast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.ui.mainactivity.MainActivity
import javax.inject.Inject


class NotificationUtils @Inject constructor(
    private val context: Context
) {
    fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.channel_name),

                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description =
                context.getString(R.string.notification_channel_description)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    /*
     * A Kotlin extension function for AndroidX's NotificationCompat that sends our Geofence
     * entered notification.  It sends a custom notification based on the name string associated
     * with the LANDMARK_DATA from GeofencingConstatns in the GeofenceUtils file.
     */
    fun sendGeofenceEnteredNotification(
        locationId: Long,
        detail: String
    ) {

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val contentIntent = Intent(context, MainActivity::class.java)
        contentIntent.putExtra("locationId" , locationId)
        //contentIntent.putExtra(GeofencingConstants.EXTRA_GEOFENCE_INDEX, foundIndex)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val mapImage = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.ic_location_on_red_24dp
        )
        val bigPicStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(mapImage)
            .bigLargeIcon(null)

        // We use the name resource ID from the LANDMARK_DATA along with content_text to create
        // a custom message when a Geofence triggers.
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.content_text, detail))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentPendingIntent)
            .setSmallIcon(R.drawable.ic_location_on_red_24dp)
            .setStyle(bigPicStyle)
            .setLargeIcon(mapImage)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    companion object {
        private const val NOTIFICATION_ID = 33
        private const val CHANNEL_ID = "GeofenceChannel"
    }
}