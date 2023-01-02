package com.example.kmitlcompanion.ui.mapboxview.utils

import android.location.Location
import com.example.kmitlcompanion.domain.model.MapPoint
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import javax.inject.Inject

class MapperUtils @Inject constructor() {

    fun mapToFeatureCollections(points: List<MapPoint>): FeatureCollection {
        return FeatureCollection.fromFeatures(points.map { getFeature(it) })
    }

    private fun getFeature(mapPoint: MapPoint): Feature {
        val point = Point.fromLngLat(mapPoint.longitude, mapPoint.latitude)
        val feature = Feature.fromGeometry(point)
        feature.addStringProperty("place", mapPoint.place)
        feature.addStringProperty("description", mapPoint.description)
        feature.addNumberProperty("id", mapPoint.id)
        return feature
    }



}