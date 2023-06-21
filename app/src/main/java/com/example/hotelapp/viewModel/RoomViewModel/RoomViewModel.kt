package com.example.hotelapp.viewModel.RoomViewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelapp.model.RoomResponse
import com.example.hotelapp.repository.HotelRepository
import com.example.hotelapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class RoomViewModel(val app: Application, val repository: HotelRepository) : ViewModel() {
    var roomList : MutableLiveData<Resource<RoomResponse>> = MutableLiveData()
    var HOTEL_ID : String? = null;
    public fun setHotelId(hotelId: String) {
        this.HOTEL_ID = hotelId
        getAllRooms()
    }
    private fun getAllRooms() = viewModelScope.launch(Dispatchers.IO) {
        roomList.postValue(Resource.Loading())
        val response = repository.getRoomWithHotelId(HOTEL_ID!!)
        roomList.postValue(handleRoomResponse(response))
    }
    private fun handleRoomResponse(response: Response<RoomResponse>) : Resource<RoomResponse>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
}