package com.example.kmitlcompanion.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Suppress("EXPERIMENTAL_API_USAGE")
@Serializable
data class MapPoint(
    val description: String,
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val name: String
){

    fun toJson(): String {
        //Notice we call a serializer method which is autogenerated from our class
        //once we have added the annotation to it

        return Json.encodeToString(serializer(), this)
    }
}