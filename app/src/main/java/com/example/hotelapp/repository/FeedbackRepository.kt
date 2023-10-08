package com.example.hotelapp.repository

import com.example.hotelapp.api.RetrofitInstance
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.model.FeedbackBody

class FeedbackRepository(val db : HotelDatabase) {
    suspend fun addFeedback(auth : String, user: String, hotel : String, feedbackBody: FeedbackBody) = RetrofitInstance.api.addFeedback(auth,user,hotel,feedbackBody)
}