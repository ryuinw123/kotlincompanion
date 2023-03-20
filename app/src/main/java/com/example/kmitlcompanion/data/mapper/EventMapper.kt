package com.example.kmitlcompanion.data.mapper

import android.util.Log
import com.example.kmitlcompanion.data.model.EventAreaData
import com.example.kmitlcompanion.domain.model.EventArea
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
            type = it.type,
            url = it.url,
        )
    }

}