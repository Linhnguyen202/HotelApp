package com.example.hotelapp.model

data class MessageResponse(
    val __v: Int,
    val _id: String,
    val chatId: String,
    val createdAt: String,
    val senderId: String,
    val text: String,
    val updatedAt: String
)