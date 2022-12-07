package com.example.kmitlcompanion.data.mapper

import com.example.kmitlcompanion.data.model.MapPointData
import com.example.kmitlcompanion.domain.model.MapPoint
import javax.inject.Inject

class MapPointMapper @Inject constructor(

){
    fun mapToDomain(it: MapPointData): MapPoint {
        return MapPoint(
            id = it.id,
            description = it.description,
            latitude = it.latitude,
            longitude = it.longitude,
            address = it.address,
            imageLink = it.imageLink,
            type = it.type,
            place = it.place
        )
    }

}