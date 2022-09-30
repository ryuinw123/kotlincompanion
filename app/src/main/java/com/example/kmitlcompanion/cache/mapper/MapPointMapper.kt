package com.example.kmitlcompanion.cache.mapper

import com.example.kmitlcompanion.cache.entities.MapPointEntity
import com.example.kmitlcompanion.data.model.MapPointData
import javax.inject.Inject

class MapPointMapper @Inject constructor(

) {
    fun mapToData(it: MapPointEntity): MapPointData {
        return MapPointData(
            id = it.id,
            description = it.description,
            latitude = it.latitude,
            longitude = it.longitude,
            name = it.name
        )
    }

    fun mapToEntity(it: MapPointData): MapPointEntity {
        return MapPointEntity(
            id = it.id,
            description = it.description,
            latitude = it.latitude,
            longitude = it.longitude,
            name = it.name
        )
    }
}