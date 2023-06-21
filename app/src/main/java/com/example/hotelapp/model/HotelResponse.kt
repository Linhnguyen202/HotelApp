package com.example.hotelapp.model

data class HotelResponse(
    val result: List<Hotel>,
    val status: String,
    val total_result: Int
)