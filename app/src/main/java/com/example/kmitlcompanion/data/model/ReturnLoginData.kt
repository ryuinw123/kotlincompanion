package com.example.kmitlcompanion.data.model

data class ReturnLoginData(
    val status : Int,
    val refreshToken: String,
    val email: String,
)
