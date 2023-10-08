package com.example.hotelapp.model

data class Chat (
    var id: Int = 0,
    val username: String,
    val text: String,
    var isSelf: Boolean = false
)