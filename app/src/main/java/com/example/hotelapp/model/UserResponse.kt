package com.example.hotelapp.model

data class UserResponse(
    val status: String,
    val token: String,
    val user: User
)