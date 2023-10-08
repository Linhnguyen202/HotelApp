package com.example.hotelapp.viewModel.FeedbackViewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelapp.model.FeedbackBody
import com.example.hotelapp.model.FeedbackResponse
import com.example.hotelapp.model.Hotel
import com.example.hotelapp.repository.FeedbackRepository
import com.example.hotelapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class FeedbackViewModel(val app: Application, val repository: FeedbackRepository) : ViewModel(){
    val userFeedback : MutableLiveData<Resource<FeedbackResponse>> = MutableLiveData()

    fun sendFeedback(auth: String, user: String, hotel: String, feedbackBody: FeedbackBody) = viewModelScope.launch {
        userFeedback.postValue(Resource.Loading())
        val response = repository.addFeedback(auth,user,hotel,feedbackBody)
        userFeedback.postValue(handleFeedbackResponse(response))
    }
    private fun<T>handleFeedbackResponse(response : Response<T>) : Resource<T>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
}