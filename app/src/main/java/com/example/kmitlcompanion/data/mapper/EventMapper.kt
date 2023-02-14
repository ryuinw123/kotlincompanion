package com.example.kmitlcompanion.data.mapper

import com.example.kmitlcompanion.data.model.EventAreaData
import com.example.kmitlcompanion.data.model.MapPointData
import com.example.kmitlcompanion.domain.model.EventArea
import com.example.kmitlcompanion.domain.model.MapPoint
import javax.inject.Inject

class EventMapper @Inject constructor(

){
    fun mapToDomain(it: EventAreaData): EventArea {
        return EventArea(
            name = it.name,
            description = it.description,
            id = it.id,
            startTime = it.startTime,
            endTime = it.endTime,
            area = it.area,
            imageLink = it.imageLink,
        )
    }

}