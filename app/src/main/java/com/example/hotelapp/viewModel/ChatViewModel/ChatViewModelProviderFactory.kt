package com.example.hotelapp.viewModel.ChatViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapp.repository.AuthRepository
import com.example.hotelapp.repository.ChatRepository
import com.example.hotelapp.repository.HotelRepository
import com.example.hotelapp.viewModel.UserViewModel.UserViewModel

class ChatViewModelProviderFactory (val app : Application, val chatRepository: ChatRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(app,chatRepository) as T
    }
}