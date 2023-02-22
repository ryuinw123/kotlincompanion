package com.example.kmitlcompanion.ui.mapboxview.utils

import com.example.kmitlcompanion.domain.model.EventArea
import com.mapbox.geojson.Point
import javax.inject.Inject
import kotlin.math.*

class TagZoomCalculateUtils @Inject constructor() {


    fun isWithinKm(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Boolean {
        val earthRadius = 6371 // Radius of the earth in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distance = earthRadius * c

        return distance <= DISTANCE
    }

    fun calculateMaxDistance(setOfMarkerPosition: List<Point?>?) : Double{
        var maxDistance = 0.0
        if (setOfMarkerPosition != null){
            for (i in 0 until setOfMarkerPosition?.size!! - 1) {
                for (j in i + 1 until setOfMarkerPosition.size) {
                    val distance = distanceBetween(setOfMarkerPosition[i]!!, setOfMarkerPosition[j]!!)
                    if (distance > maxDistance) {
                        maxDistance = distance
                    }
                }
            }
        }
        return maxDistance
    }

    private fun distanceBetween(p1: Point, p2: Point): Double {
        val earthRadius = 6371.0 //in kilometers
        val lat1 = p1.latitude() * Math.PI / 180.0
        val lat2 = p2.latitude() * Math.PI / 180.0
        val lon1 = p1.longitude() * Math.PI / 180.0
        val lon2 = p2.longitude() * Math.PI / 180.0
        val deltaLat = lat2 - lat1
        val deltaLon = lon2 - lon1
        val a = sin(deltaLat / 2) * sin(deltaLat / 2) +
                cos(lat1) * cos(lat2) *
                sin(deltaLon / 2) * sin(deltaLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }


    fun calculateZoomLevel(distance: Double, screenWidth: Int): Double {
        val equatorLength = 40075004.0 // in meters
        val widthInMeters = distance * screenWidth
        var zoomLevel = ln(equatorLength / widthInMeters) / ln(2.35)

        if (zoomLevel.isInfinite()){
            zoomLevel = 18.0
        }
        return zoomLevel
    }

    fun filterEventAreasWithin(currentPosition: Point, eventAreaList : List<EventArea>?): List<EventArea>{
        return filterEventAreasWithinLoop(currentPosition,eventAreaList)
    }

    private fun filterEventAreasWithinLoop(currentPosition: Point, listOfEventAreaPolygon: List<EventArea>?): List<EventArea> {
        val filteredEventAreas = mutableListOf<EventArea>()
        if (listOfEventAreaPolygon != null) {
            for (eventArea in listOfEventAreaPolygon) {
                val area = eventArea.area
                var isWithin20km = false
                for (i in area.indices) {
                    val point = area[i]
                    val distance = distanceBetween(currentPosition, point)
                    if (distance <= DISTANCE) {
                        isWithin20km = true
                        break
                    }
                }
                if (isWithin20km) {
                    filteredEventAreas.add(eventArea)
                }
            }
        }
        return filteredEventAreas
    }

    fun extractPoints(eventAreas: List<EventArea>): List<Point>? {
        return eventAreas.flatMap { it.area }
    }

    companion object {
        const val DISTANCE = 20
    }



}