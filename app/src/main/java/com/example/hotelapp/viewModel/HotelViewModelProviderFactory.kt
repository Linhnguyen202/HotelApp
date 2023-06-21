package com.example.hotelapp.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapp.repository.HotelRepository

@Suppress("UNCHECKED_CAST")
class HotelViewModelProviderFactory(val app : Application, val hotelRepository: HotelRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HotelViewModel(app,hotelRepository) as T
    }
}