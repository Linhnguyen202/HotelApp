package com.example.hotelapp.repository

import com.example.hotelapp.api.RetrofitInstance
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.model.FavorBody

class FavorRepository(val db : HotelDatabase) {
    suspend fun makeFavorHotel(userId: String, author: String, body: FavorBody) =RetrofitInstance.api.makeFavorHotel(userId,author, body)

    suspend fun getFavorHotel(userId: String, author: String) = RetrofitInstance.api.getFavorHotel(userId, author)

    suspend fun deleteFavor(userId: String, favorId: String, author: String) = RetrofitInstance.api.deleteFavorHotel(userId, favorId, author)

    suspend fun checkfavor(userId: String, favorId: String, author: String)  = RetrofitInstance.api.checkfavor(userId, favorId, author)
}