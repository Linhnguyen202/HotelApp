package com.example.hotelapp.model

data class bookingBody(var name: String,
                       var totalPrice: Number,
                       var price: Number,
                       var status : String,
                       var description: String,
                       var numberRoom: Int,
                       var guestNumber: Int,
                       var paymentType: String,
                       val startDate: String,
                       val endDate: String,
                       var user: String,
                       var hotel: String, var room: String)
