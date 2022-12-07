package com.example.kmitlcompanion.data.model

data class MapPointData(
    val description : String,
    val address : String,
    val id: Long,
    val latitude: Double,
    val longitude : Double,
    val place: String,
    val imageLink : List<String>,
    val type : String,
)
