package com.example.kmitlcompanion.foreground.utils

import com.mapbox.geojson.Point
import com.mapbox.turf.TurfMeasurement
import javax.inject.Inject

class MeasurementUtils @Inject constructor(

) {
    fun distance(point1: Point, point2: Point): Double {
        return TurfMeasurement.distance(point1, point2)
    }

    fun bearing(point1: Point, point2: Point): Double {
        return TurfMeasurement.bearing(point1, point2)
    }
}