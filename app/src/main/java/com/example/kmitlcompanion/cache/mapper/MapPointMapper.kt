package com.example.kmitlcompanion.cache.mapper

import com.example.kmitlcompanion.cache.entities.MapPointEntity
import com.example.kmitlcompanion.data.model.MapPointData
import javax.inject.Inject

class MapPointMapper @Inject constructor(

) {
    fun mapToData(it: MapPointEntity): MapPointData {
        return MapPointData(
            id = it.id,
            name = it.name,
            description = it.description,
            latitude = it.latitude,
            longitude = it.longitude,
            place = it.place,
            address = it.address,
            imageLink = listOf(),
            type = it.type
        )
    }

    fun mapToEntity(it: MapPointData): MapPointEntity {
        return MapPointEntity(
            id = it.id,
            name = it.name,
            description = it.description,
            latitude = it.latitude,
            longitude = it.longitude,
            place = it.place,
            address = it.address,
            type = it.type,
            imageLink = it.imageLink[0]
        )
    }
}