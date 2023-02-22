package com.example.kmitlcompanion.ui.mapboxview.utils

import com.mapbox.geojson.Point
import javax.inject.Inject

class PolygonUtils @Inject constructor() {

    fun getCenter(points: List<Point>): Point {
        var latSum = 0.0
        var lngSum = 0.0
        val size = points.size

        for (point in points) {
            latSum += point.latitude()
            lngSum += point.longitude()
        }

        val lat = latSum / size
        val lng = lngSum / size

        return Point.fromLngLat(lng,lat)
    }

}