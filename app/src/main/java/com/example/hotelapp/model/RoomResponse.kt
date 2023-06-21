package com.example.hotelapp.model

data class RoomResponse(
    val result: List<Room>,
    val status: String,
    val total_results: Int
)