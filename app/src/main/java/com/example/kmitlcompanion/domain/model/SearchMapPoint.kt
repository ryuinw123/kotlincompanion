package com.example.kmitlcompanion.domain.model

import java.io.Serializable


@Suppress("EXPERIMENTAL_API_USAGE")
data class SearchMapPoint(
    val name:String,
    val description : String,
    val address : String,
    val id: Long,
    val latitude: Double,
    val longitude : Double,
    val place: String,
    val imageLink : List<String>,
    val type : String
) : Serializable