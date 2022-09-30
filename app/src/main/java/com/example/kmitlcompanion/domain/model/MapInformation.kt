package com.example.kmitlcompanion.domain.model

data class MapInformation(
    val mapPoints: List<MapPoint>,
    val source: Source,
    val timeStamp: Long
)