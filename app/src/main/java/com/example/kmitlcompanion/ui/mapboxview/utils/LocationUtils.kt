package com.example.kmitlcompanion.ui.mapboxview.utils

import com.example.kmitlcompanion.domain.model.MapPoint
import javax.inject.Inject

class LocationUtils @Inject constructor(

) {
    fun queryLocation(locationId : Long , mapList : List<MapPoint>) : MapPoint? {
        for (mapPoint in mapList) {
            if (mapPoint.id == locationId)
                return mapPoint
        }
        return null
    }
}