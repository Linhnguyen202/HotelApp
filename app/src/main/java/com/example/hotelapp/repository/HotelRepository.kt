package com.example.hotelapp.repository

import com.example.hotelapp.api.RetrofitInstance
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.model.bookingBody

class HotelRepository(val db : HotelDatabase) {
    suspend fun getHotelData(pageNumber : Int, limitNumber : Int) = RetrofitInstance.api.getAllHotel(pageNumber, limitNumber)

    suspend fun getTypeHotel(typeData: String,pageNumber : Int, limitNumber : Int) = RetrofitInstance.api.getTypeHotel(typeData,pageNumber, limitNumber)

    suspend fun getRoomWithHotelId(hotelId: String) = RetrofitInstance.api.getRoomWithHotelId(hotelId)

    suspend fun searchHotel(cityQuery: String, guestNumber: Int) = RetrofitInstance.api.searchHotel(cityQuery,guestNumber)

    suspend fun userBooking(userId: String,header: String,bookingBody: bookingBody) = RetrofitInstance.api.makeBooking(userId,header,bookingBody)
}