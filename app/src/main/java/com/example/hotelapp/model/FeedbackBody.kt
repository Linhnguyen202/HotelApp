package com.example.hotelapp.model

data class FeedbackBody(
    val comment: String,
    val hotel: String,
    val rate: Double,
    val room: String,
    val user: String
)