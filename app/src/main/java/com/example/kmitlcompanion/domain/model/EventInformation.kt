package com.example.kmitlcompanion.domain.model

data class EventInformation(
    val eventPoints: List<EventArea>,
    val source: Source,
    val timeStamp: Long
)