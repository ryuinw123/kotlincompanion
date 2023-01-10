package com.example.kmitlcompanion.foreground

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleService
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.domain.model.MapInformation
import com.example.kmitlcompanion.domain.usecases.GetMapLocations
import com.example.kmitlcompanion.foreground.utils.MeasurementUtils
import com.example.kmitlcompanion.ui.notification.NotificationManagerCalling
import com.google.android.gms.location.LocationServices
import com.mapbox.geojson.Point
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.observers.DisposableObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MapService : Service() {

    private val binder = MapBinder()

    private lateinit var locationHelper: SecretLocation

    @Inject
    lateinit var measurementUtils: MeasurementUtils

    @Inject
    lateinit var getMapLocations: GetMapLocations

    @Inject
    lateinit var notificationManagerCalling: NotificationManagerCalling




    var mapInformation : MapInformation? = null
        set(value) {
            field = value
        }

    var location : Point? = null
        set(value){
            field = value

            mapInformation?.let {
                filterArea(it,value!!)
            }
            //secretMap.filterArea(value!!)
        }

    private fun filterArea(mapInformation: MapInformation, point: Point) {
        mapInformation.mapPoints.forEach {
            val mapPoint = Point.fromLngLat(it.longitude , it.latitude)
            val distance = measurementUtils.distance(point , mapPoint)
            if (distance <= 5) {
                Log.d("FilterFound", it.place)
                notificationManagerCalling.startNotify()
            }
        }
    }


    override fun onCreate() {
        super.onCreate()
        Log.d("Service" , "Service Create")

        locationHelper = SecretLocation(this )
        locationHelper.run()
        notificationManagerCalling.createChannel()



    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }





    inner class MapBinder : Binder() {
        fun getService() : MapService = this@MapService
    }

    private fun downloadLocation() {
        getMapLocations.execute(object : DisposableObserver<MapInformation>() {
            override fun onComplete() {
                println("Complete")
            }

            override fun onNext(t: MapInformation) {
                println("FromObserver")
                println(t)
                mapInformation = t
            }

            override fun onError(e: Throwable) {
                println("Error")
            }

        })
    }


    override fun onBind(intent: Intent?): IBinder? {

        downloadLocation()

       return binder
    }



}