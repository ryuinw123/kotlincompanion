package com.example.kmitlcompanion.domain.model

data class LoginData(
    val status : Int,
    val refreshToken: String,
    val email: String,
)
