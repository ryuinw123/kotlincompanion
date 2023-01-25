package com.example.kmitlcompanion.ui.mapboxview.utils

import com.example.kmitlcompanion.domain.model.MapPoint
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import javax.inject.Inject

class MapperUtils @Inject constructor() {

    fun mapToFeatureCollections(points: List<MapPoint>): FeatureCollection {
        return FeatureCollection.fromFeatures(points.map { getFeature(it) })
    }

    fun mapToCircleFeatureCollections(points : List<MapPoint>) : FeatureCollection {
        return FeatureCollection.fromFeatures(points.map { getCircleFeature(it) })
    }

    private fun getCircleFeature(mapPoint: MapPoint): Feature {
        val point = Point.fromLngLat(mapPoint.longitude, mapPoint.latitude)
        val circleJson = ("{ type: 'Feature', geometry: { type: 'Polygon', coordinates: [ ${
            createCircleCoordinates(
                point,
                5.0
            )
        } ] }, "
                + "properties: {}}")

        return Feature.fromJson(circleJson)

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

    private fun createCircleCoordinates(center : Point, radiusInKm:Double , points : Int = 64) : List<List<Double>> {


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



}