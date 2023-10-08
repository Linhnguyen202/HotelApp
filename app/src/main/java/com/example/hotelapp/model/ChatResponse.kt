package com.example.hotelapp.model

data class ChatResponse(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val members: List<String>,
    val updatedAt: String
)