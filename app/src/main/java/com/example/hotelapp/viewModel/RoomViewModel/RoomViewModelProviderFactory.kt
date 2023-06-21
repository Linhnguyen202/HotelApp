package com.example.hotelapp.viewModel.RoomViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapp.repository.HotelRepository
import com.example.hotelapp.viewModel.HotelViewModel

@Suppress("UNCHECKED_CAST")
class RoomViewModelProviderFactory(val app : Application, val hotelRepository: HotelRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RoomViewModel(app,hotelRepository) as T
    }
}