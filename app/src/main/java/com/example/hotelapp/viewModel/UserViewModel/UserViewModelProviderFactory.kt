package com.example.hotelapp.viewModel.UserViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapp.repository.HotelRepository
import com.example.hotelapp.viewModel.HotelViewModel

class UserViewModelProviderFactory (val app : Application, val hotelRepository: HotelRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(app,hotelRepository) as T
    }
}