package com.example.hotelapp.repository

import com.example.hotelapp.api.RetrofitInstance
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.model.Booking
import com.example.hotelapp.model.bookingBody

class HotelRepository(val db : HotelDatabase) {
    suspend fun getHotelData(pageNumber : Int, limitNumber : Int) = RetrofitInstance.api.getAllHotel(pageNumber, limitNumber)

    suspend fun getTypeHotel(typeData: String,pageNumber : Int, limitNumber : Int) = RetrofitInstance.api.getTypeHotel(typeData,pageNumber, limitNumber)

    suspend fun getRoomWithHotelId(hotelId: String) = RetrofitInstance.api.getRoomWithHotelId(hotelId)

    suspend fun searchHotel(cityQuery: String, guestNumber: Int, pageNumber: Int, limitNumber: Int) = RetrofitInstance.api.searchHotel(cityQuery,guestNumber,pageNumber, limitNumber)

    suspend fun userBooking(userId: String,header: String,bookingBody: bookingBody) = RetrofitInstance.api.makeBooking(userId,header,bookingBody)

    suspend fun getUserBooking(userId: String,header: String) = RetrofitInstance.api.getUserBooking(userId,header)

    suspend fun getUserCancelBooking(userId: String,header: String) = RetrofitInstance.api.getUserCancelBooking(userId,header)

    suspend fun cancelUserBooking(userId: String, bookingId: String, header: String) = RetrofitInstance.api.cancelBooking(userId,bookingId, header)
}