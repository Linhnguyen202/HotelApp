package com.example.hotelapp.api

import com.example.hotelapp.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HotelApi {
    @GET("hotels")
    suspend fun getAllHotel(
        @Query("page")
        pageNumber: Int = 1,
        @Query("limit")
        limitNumber: Int = 10
    ) : Response<HotelResponse>

    @GET("hotels")
    suspend fun getTypeHotel(
        @Query("type")
        typeData: String = "Popular",
        @Query("page")
        pageNumber: Int = 1,
        @Query("limit")
        limitNumber: Int = 10
    ) : Response<HotelResponse>

    @GET("hotels/search")
    suspend fun searchHotel(
        @Query("city")
        cityQuery: String? = null,
        @Query("guestNumber")
        guestQuery: Int? = null,
    ) : Response<HotelResponse>

    @GET("hotels/{hotelId}/rooms")
    suspend fun getRoomWithHotelId(
       @Path("hotelId")
       hotelId : String?
    ) : Response<RoomResponse>

    @POST("user/signUp")
    suspend fun registerUSer(
        @Body body: registerBody
    ) : Response<UserResponse>

    @POST("user/signIn")
    suspend fun logInUser(
        @Body body: SignInBody
    ) : Response<UserResponse>

    @POST("user/{userId}/booking")
    suspend fun makeBooking(
        @Path("userId") userId: String,
        @Header("Authorization") header: String,
        @Body body: bookingBody
    ) : Response<BookingResponse>

    @POST("user/{userId}/favorHotel")
    suspend fun makeFavorHotel(
        @Path("userId") userId: String,
        @Header("Authorization") header: String,
        @Body body: FavorBody
    ) : Response<PostFavorResponse>

    @GET("user/{userId}/favorHotel")
    suspend fun getFavorHotel(
        @Path("userId") userId: String,
        @Header("Authorization") header: String,
    ) : Response<GetFavorResponse>

    @DELETE("user/{userId}/favorHotel/{favorId}")
    suspend fun deleteFavorHotel(
        @Path("userId") userId: String,
        @Path("favorId") favorId: String,
        @Header("Authorization") header: String,
    ) : Response<StatusFavorResponse>

    @GET("user/{userId}/favorHotel/{favorId}")
    suspend fun checkfavor(
        @Path("userId") userId: String,
        @Path("favorId") favorId: String,
        @Header("Authorization") header: String,
    ) : Response<StatusFavorResponse>
}