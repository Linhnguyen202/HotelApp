package com.example.hotelapp.repository

import com.example.hotelapp.api.RetrofitInstance
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.model.ChatBody
import com.example.hotelapp.model.MessageBody

class ChatRepository(val db : HotelDatabase) {
    suspend fun makeChat(auth: String,chatBody: ChatBody) = RetrofitInstance.api.makeChat(auth,chatBody)

    suspend fun sendMessage(auth: String,messageBody: MessageBody) = RetrofitInstance.api.sendMessage(auth,messageBody)

    suspend fun getListMessage(auth: String,chatId: String) = RetrofitInstance.api.getMessage(auth,chatId)
}