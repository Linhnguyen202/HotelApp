package com.example.hotelapp.model

data class UserBookingResponse(
    val status: String,
    val booking: List<Booking>
)