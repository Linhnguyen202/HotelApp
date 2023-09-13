package com.example.hotelapp.repository

import android.provider.ContactsContract.CommonDataKinds.Email
import com.example.hotelapp.api.RetrofitInstance
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.model.SignInBody
import com.example.hotelapp.model.User
import com.example.hotelapp.model.registerBody

class AuthRepository(val db : HotelDatabase) {
   suspend fun registerUser(body: registerBody) = RetrofitInstance.api.registerUSer(body)

   suspend fun signInUser(signInBody: SignInBody) = RetrofitInstance.api.logInUser(signInBody)

    suspend fun updateUser(userId : String,user: User) = RetrofitInstance.api.updateProfile(userId,user)
}