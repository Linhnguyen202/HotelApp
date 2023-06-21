package com.example.hotelapp.viewModel.FavorViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapp.repository.FavorRepository
import com.example.hotelapp.repository.HotelRepository
import com.example.hotelapp.viewModel.HotelViewModel

class FavorViewModelProviderFactory(val app : Application, val favorRepository: FavorRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavorViewModel(app,favorRepository) as T
    }
}