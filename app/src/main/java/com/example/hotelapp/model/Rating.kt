package com.example.hotelapp.model

data class Rating(
    val comment : String,
    val rate : Number,
    val user : User,
    val hotel : Hotel,
    val room: Room
)
