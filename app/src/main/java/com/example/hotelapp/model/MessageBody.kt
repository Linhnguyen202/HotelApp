package com.example.hotelapp.model

data class MessageBody(
    val chatId: String,
    val senderId: String,
    val text: String
)