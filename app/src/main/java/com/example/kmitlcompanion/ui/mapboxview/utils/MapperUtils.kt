package com.example.kmitlcompanion.ui.mapboxview.utils

import com.example.kmitlcompanion.domain.model.EventArea
import com.example.kmitlcompanion.domain.model.MapPoint
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import javax.inject.Inject

class MapperUtils @Inject constructor() {

    fun mapToFeatureCollections(points: List<MapPoint>): FeatureCollection {
        return FeatureCollection.fromFeatures(points.map { getFeature(it) })
    }

    fun mapToAreaFeatureCollections(events : List<EventArea>) : FeatureCollection {
        return FeatureCollection.fromFeatures(events.map { getAreaFeature(it) })
    }

    private fun getAreaFeature(eventArea: EventArea): Feature {
        val polygon = Polygon.fromLngLats(listOf( eventArea.area))
        val feature = Feature.fromGeometry(polygon)
        feature.addStringProperty("name", eventArea.name)
        feature.addStringProperty("description", eventArea.description)
        feature.addNumberProperty("id", eventArea.id)
        feature.addStringProperty("imageLink",eventArea.imageLink.toString())

        /*val areaJson = ("{ type: 'Feature', geometry: { type: 'Polygon', coordinates: [ ${
             getDoubleFromPointArea(eventArea.area)
        } ] }, "
                + "properties: { 'name' : '${eventArea.name}' , 'description' : '${eventArea.description}' , 'id' : '${eventArea.id}' , 'imageLink' : '${eventArea.imageLink}'} }")

        return Feature.fromJson(areaJson)*/

        return feature

    }

    private fun getDoubleFromPointArea( points : List<Point> ) : List<List<Double>> {
        val polygon = mutableListOf<List<Double>>()
        for (i in points) {
            polygon.add( listOf(i.longitude(),i.latitude()) )
        }
        return polygon
    }

    private fun getFeature(mapPoint: MapPoint): Feature {
        val point = Point.fromLngLat(mapPoint.longitude, mapPoint.latitude)
        val feature = Feature.fromGeometry(point)
        feature.addStringProperty("name", mapPoint.name)
        feature.addStringProperty("place", mapPoint.place)
        feature.addStringProperty("address", mapPoint.address)
        feature.addStringProperty("description", mapPoint.description)
        feature.addNumberProperty("id", mapPoint.id)
        feature.addStringProperty("imageLink",mapPoint.imageLink.toString())
        return feature
    }

    fun createCircleCoordinates(center : Point, radiusInKm:Double , points : Int = 64) : List<List<Double>> {


        val km = radiusInKm

        val ret = mutableListOf<List<Double>>()
        val distanceX = km / (111.320 * Math.cos(center.latitude() * Math.PI / 180))
        val distanceY = km / 110.574


        var theta: Double
        var x: Double
        var y: Double
        for (i in 0 until points) {
            theta = (i.toDouble() / points) * (2 * Math.PI)
            x = distanceX * Math.cos(theta)
            y = distanceY * Math.sin(theta)

            ret.add(listOf(center.longitude() + x, center.latitude() + y))
        }
        ret.add(ret[0])

        return ret
    }

    fun doublePolygonToPoint(polygon :List<List<Double>>)  : List<List<Point>> {

        val mapboxPoint = mutableListOf<List<Point>>()
        val polygonPoint = mutableListOf<Point>()

        for (i in polygon.indices) {
            val point = Point.fromLngLat(polygon[i][0] , polygon[i][1])
            polygonPoint.add(point)
        }
        mapboxPoint.add(polygonPoint)

        return mapboxPoint

    }





}