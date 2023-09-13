package com.example.hotelapp.viewModel.UserViewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelapp.model.*
import com.example.hotelapp.repository.AuthRepository
import com.example.hotelapp.repository.HotelRepository
import com.example.hotelapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class UserViewModel(val app: Application, val hotelRepository: HotelRepository): ViewModel() {
    val userBooking : MutableLiveData<Resource<BookingResponse>> = MutableLiveData()
    val userBookingList : MutableLiveData<Resource<UserBookingResponse>> = MutableLiveData()
    val cancelUserBookng : MutableLiveData<Resource<BookingResponse>> = MutableLiveData()

    public fun getUserBookingList(userId: String, header: String) = viewModelScope.launch {
        userBookingList.postValue(Resource.Loading())
        val response = hotelRepository.getUserBooking(userId, header)
        userBookingList.postValue(handleUserBooking(response))
    }

    public fun getUserCancelBookingList(userId: String, header: String) = viewModelScope.launch {
        userBookingList.postValue(Resource.Loading())
        val response = hotelRepository.getUserCancelBooking(userId, header)
        userBookingList.postValue(handleUserBooking(response))
    }

    public fun makeUserBooking(userId: String, header: String,body: bookingBody) = viewModelScope.launch {
        userBooking.postValue(Resource.Loading())
        val response = hotelRepository.userBooking(userId,header, body)
        userBooking.postValue(handleUserBooking(response))
    }
    public fun cancelUserBooking(userId: String,bookingId: String, header: String) = viewModelScope.launch {
        cancelUserBookng.postValue(Resource.Loading())
        val response = hotelRepository.cancelUserBooking(userId, bookingId, header)
        cancelUserBookng.postValue(handleUserBooking(response))
    }
    private fun<T>handleUserBooking(response : Response<T>) : Resource<T>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
}