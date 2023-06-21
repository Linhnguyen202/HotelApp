package com.example.hotelapp.repository

import com.example.hotelapp.api.RetrofitInstance
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.model.SignInBody
import com.example.hotelapp.model.registerBody

class AuthRepository(val db : HotelDatabase) {
   suspend fun registerUser(body: registerBody) = RetrofitInstance.api.registerUSer(body)

    suspend fun signInUser(body: SignInBody) = RetrofitInstance.api.logInUser(body)
}