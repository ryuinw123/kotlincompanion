package com.example.kmitlcompanion.domain.model

import com.mapbox.maps.plugin.annotation.generated.PointAnnotation

data class ActivePoint(
    val mapPoint: MapPoint,
    val pointAnnotation: PointAnnotation
)
