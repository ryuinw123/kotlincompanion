package com.example.kmitlcompanion.ui.mapboxview.broadcast

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.DEFAULT_SOUND
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.example.kmitlcompanion.ui.mainactivity.MainActivity
import javax.inject.Inject


class NotificationUtils @Inject constructor(
    private val context: Context
) {
    fun createChannel(channelId : String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            val notificationChannel = NotificationChannel(
                channelId,
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
                context.getString(R.string.noti_tracking)

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
        detail: String,
        channelId: String
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val contentIntent = Intent(context, MainActivity::class.java)
        contentIntent.putExtra("locationId" , locationId)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            GEO_NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val mapImage = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.ic_event_48px
        )
        val bigPicStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(mapImage)
            .bigLargeIcon(null)

        // We use the name resource ID from the LANDMARK_DATA along with content_text to create
        // a custom message when a Geofence triggers.
        val builder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(context.getString(R.string.noti_event))
            .setContentText(context.getString(R.string.content_text, detail))
            .setDefaults(3)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(contentPendingIntent)
            .setSmallIcon(R.drawable.ic_event_48px)
            //.setStyle(bigPicStyle)
            .setLargeIcon(mapImage)

        Log.d("test_noti","sendGeofenceEnteredNotification")

        notificationManager.notify(GEO_NOTIFICATION_ID, builder.build())
    }

    fun sendGeofenceEnteredNotificationWithUrl(
        locationId: Long,
        channelId: String,
        url : String
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //val contentIntent = Intent(context, MainActivity::class.java)
        val contentIntent = Intent(Intent.ACTION_VIEW,Uri.parse(url))
        contentIntent.putExtra("locationId" , locationId)
        //contentIntent.putExtra(GeofencingConstants.EXTRA_GEOFENCE_INDEX, foundIndex)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            GEO_NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val mapImage = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.ic_baseline_link_24
        )
        val bigPicStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(mapImage)
            .bigLargeIcon(null)

        // We use the name resource ID from the LANDMARK_DATA along with content_text to create
        // a custom message when a Geofence triggers.
        val builder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(context.getString(R.string.noti_event_url))
            .setContentText(context.getString(R.string.content_text_url, url))
            .setDefaults(3)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(contentPendingIntent)
            .setSmallIcon(R.drawable.ic_baseline_link_24)
            //.setStyle(bigPicStyle)
            .setLargeIcon(mapImage)

        Log.d("test_noti","sendGeofenceEnteredNotification")

        notificationManager.notify(GEO_NOTIFICATION_URL_ID, builder.build())
    }


    fun createNotification(channelId: String): Notification {


        val mapImage = BitmapFactory.decodeResource(
            context.resources,
            R.mipmap.ic_launcher_round
        )
        val bigPicStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(mapImage)
            .bigLargeIcon(null)

        // We use the name resource ID from the LANDMARK_DATA along with content_text to create
        // a custom message when a Geofence triggers.

        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.noti_tracking))
            .setSmallIcon(R.mipmap.ic_launcher_round)
            //.setStyle(bigPicStyle)
            .setLargeIcon(mapImage)
            .build()
    }


    companion object {
        const val GEO_NOTIFICATION_ID = 33
        const val GEO_NOTIFICATION_URL_ID = 69
        const val LOCATION_NOTIFICATION_ID = 34
        const val GEO_CHANNEL_ID = "GeofenceChannel"
        const val LOCATION_CHANNEL_ID = "LocationChannel"
    }
}