package com.example.hotelapp.viewModel.FeedbackViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapp.repository.FeedbackRepository

class FeedbackViewModelProviderFactory(val app : Application, val feedbackRepository: FeedbackRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FeedbackViewModel(app,feedbackRepository) as T
    }
}