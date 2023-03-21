package com.example.kmitlcompanion.background

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.example.kmitlcompanion.domain.model.EventArea
import com.example.kmitlcompanion.ui.mapboxview.broadcast.NotificationUtils
import com.mapbox.geojson.Point
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    @Inject
    lateinit var secretLocation: SecretLocation

    @Inject
    lateinit var secretPolygon: SecretPolygon

    @Inject
    lateinit var notificationUtils: NotificationUtils

    private var isDestroyed = false

    var location: Point? = null
        set(value) {
            field = value
            if (!isDestroyed) {
                secretPolygon.filterArea(this, value ?: Point.fromLngLat(0.0, 0.0))
            }
            //secretMap.filterArea(value!!)
        }

    override fun onCreate() {
        super.onCreate()
        Log.d("Geofence", "On create")

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Geofence", "OnStartCommand")
        val eventListJson = intent?.getStringExtra("event")
        val eventList = ArrayList<EventArea>()
        val eventArray = JSONArray(eventListJson)
        Log.d("Geofence", "Step 1")
        for (i in 0 until eventArray.length()) {
            val eventObject = eventArray.getJSONObject(i)
            val id = eventObject.getLong("id")
            val pointsList = eventObject.getJSONArray("area")
            var area = ArrayList<Point>()
            for (j in 0 until pointsList.length()) {
                val pointObject = pointsList.getJSONObject(j)
                val coordinates = pointObject.getJSONArray("coordinates")
                val longitude = coordinates.getDouble(0)
                val latitude = coordinates.getDouble(1)
                area.add(Point.fromLngLat(longitude, latitude))
            }

            val name = eventObject.getString("name")
            val startTime = eventObject.getString("startTime")
            val endTime = eventObject.getString("endTime")
            val imageLinkArray = eventObject.getJSONArray("imageLink")
            val eventType = eventObject.getInt("type")
            val eventUrlArray = eventObject.getJSONArray("url")

            val imageLinks = mutableListOf<String>()
            for (i in 0 until imageLinkArray.length()) {
                imageLinks.add(imageLinkArray.getString(i))
            }

            val eventUrl = mutableListOf<String>()
            for (i in 0 until eventUrlArray.length()) {
                eventUrl.add(eventUrlArray.getString(i))
            }

            Log.d("test_noti_bug","$name create notification")

            eventList.add(
                EventArea(
                    name = name,
                    description = "",
                    id = id,
                    startTime = startTime,
                    endTime = endTime,
                    area = area,
                    imageLink = imageLinks,
                    type = eventType,
                    url = eventUrl,
                )
            )
        }


        secretLocation.start(this)
        secretPolygon.start(eventList)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationUtils.createChannel(NotificationUtils.GEO_CHANNEL_ID)
            notificationUtils.createChannel(NotificationUtils.LOCATION_CHANNEL_ID)
            val notification = notificationUtils.createNotification(NotificationUtils.LOCATION_CHANNEL_ID)
            startForeground(123, notification)
        }
        return START_STICKY
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        isDestroyed = true
        Log.d("test_bugnoti","here")
        stopForeground(true)
        stopSelf()
    }

}