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
            name = it.name
        )
    }

    fun mapToData(it: MapPoint): MapPointData {
        return MapPointData(
            id = it.id,
            description = it.description,
            latitude = it.latitude,
            longitude = it.longitude,
            name = it.name
        )
    }
}