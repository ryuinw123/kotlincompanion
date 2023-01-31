package com.example.kmitlcompanion.background

import android.content.Context
import android.util.Log
import androidx.work.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkManagerUtils @Inject constructor(
    private val context: Context
) {
    fun scheduleLocationWorker() {
        Log.d("Geofence" , "Location Inject")

        /*val locationListJson = Gson().toJson(mapInformation)

           val data = PinData.Builder()
                .putString("location_list", locationListJson)
                .build()*/


         /*val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()*/

        val geofenceWork = PeriodicWorkRequestBuilder<LocationWorker>(15,TimeUnit.MINUTES)
                //.setInputData(data)
                //.setConstraints(constraints)
            .build()


        WorkManager.getInstance(context).enqueueUniquePeriodicWork("Location",ExistingPeriodicWorkPolicy.REPLACE,geofenceWork)
    }
}