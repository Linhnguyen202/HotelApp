package com.example.hotelapp.model

data class GetFavorResponse(
    val status : String,
    val total_result: String,
    val favor : List<FavorHotel>
)
