package com.example.kmitlcompanion.foreground

import androidx.work.*
import com.example.kmitlcompanion.domain.model.MapPoint
import com.google.gson.Gson

class GeofenceWorkManager {

    companion object {
        fun scheduleGeofenceWorker(mapInformation : List<MapPoint>) {

            val locationListJson = Gson().toJson(mapInformation)

            val data = Data.Builder()
                .putString("location_list", locationListJson)
                .build()


            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val geofenceWork = OneTimeWorkRequestBuilder<GeofenceWorker>()
                .setInputData(data)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance().enqueueUniqueWork("geofence_work", ExistingWorkPolicy.REPLACE, geofenceWork)
        }
    }
}