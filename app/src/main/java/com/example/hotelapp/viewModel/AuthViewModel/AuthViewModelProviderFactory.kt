package com.example.hotelapp.viewModel.AuthViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapp.repository.AuthRepository
import com.example.hotelapp.viewModel.HotelViewModel

class AuthViewModelProviderFactory(val app : Application, val authRepository: AuthRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(app, authRepository) as T
    }
}