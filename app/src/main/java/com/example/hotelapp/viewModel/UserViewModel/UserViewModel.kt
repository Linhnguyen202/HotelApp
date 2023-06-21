package com.example.hotelapp.viewModel.UserViewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelapp.model.Booking
import com.example.hotelapp.model.BookingResponse
import com.example.hotelapp.model.bookingBody
import com.example.hotelapp.repository.HotelRepository
import com.example.hotelapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class UserViewModel(val app: Application, val hotelRepository: HotelRepository): ViewModel() {
    val userBooking : MutableLiveData<Resource<BookingResponse>> = MutableLiveData()

    public fun makeUserBooking(userId: String, header: String,body: bookingBody) = viewModelScope.launch {
        userBooking.postValue(Resource.Loading())
        val response = hotelRepository.userBooking(userId,header, body)
        userBooking.postValue(handleUserBooking(response))
    }
    private fun handleUserBooking(response : Response<BookingResponse>) : Resource<BookingResponse>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
}