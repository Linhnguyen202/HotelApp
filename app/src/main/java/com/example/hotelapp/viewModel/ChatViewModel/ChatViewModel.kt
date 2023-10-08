package com.example.hotelapp.viewModel.ChatViewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelapp.model.*
import com.example.hotelapp.repository.ChatRepository
import com.example.hotelapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class ChatViewModel(val app: Application, val chatRepository: ChatRepository): ViewModel() {
    val userChat : MutableLiveData<Resource<ChatResponse>> = MutableLiveData()
    val message : MutableLiveData<Resource<MessageResponse>> = MutableLiveData()
    val listMessage : MutableLiveData<Resource<ChatListResponse>> = MutableLiveData()
    public fun makeChat(auth: String,chatBody : ChatBody) = viewModelScope.launch {
        userChat.postValue(Resource.Loading())
        val response = chatRepository.makeChat(auth,chatBody)
        userChat.postValue(handleChatResponse(response))
    }

   fun sendMessage(auth : String,messageBody: MessageBody) = viewModelScope.launch(Dispatchers.IO) {
        message.postValue(Resource.Loading())
        val response = chatRepository.sendMessage(auth,messageBody)
        message.postValue(handleChatResponse(response))
    }


    public fun getListMessage(auth : String,chatId : String) = viewModelScope.launch {
        listMessage.postValue(Resource.Loading())
        val response = chatRepository.getListMessage(auth,chatId)
        listMessage.postValue(handleChatResponse(response))
    }

    private fun<T>handleChatResponse(response : Response<T>) : Resource<T>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
}