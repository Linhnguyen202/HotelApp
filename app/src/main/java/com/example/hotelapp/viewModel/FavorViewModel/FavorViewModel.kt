package com.example.hotelapp.viewModel.FavorViewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelapp.model.*
import com.example.hotelapp.repository.FavorRepository
import com.example.hotelapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class FavorViewModel(val app: Application, val favorRepository: FavorRepository): ViewModel() {
    val postFavorResponse : MutableLiveData<Resource<PostFavorResponse>> = MutableLiveData()
    val statusFavorResponse : MutableLiveData<Resource<StatusFavorResponse>> = MutableLiveData()
    val getFavorResponse : MutableLiveData<Resource<GetFavorResponse>> = MutableLiveData()
    val checkFavorResponse : MutableLiveData<Resource<StatusFavorResponse>> = MutableLiveData()
    public fun makeFavor(userId: String, auth: String, favorBody: FavorBody) = viewModelScope.launch {
        postFavorResponse.postValue(Resource.Loading())
        val response = favorRepository.makeFavorHotel(userId,auth,favorBody)
        postFavorResponse.postValue(handleFavorQuery<PostFavorResponse>(response))
    }
    public fun deleteFavor(userId: String, favorId : String, auth: String) = viewModelScope.launch {
        statusFavorResponse.postValue(Resource.Loading())
        val response = favorRepository.deleteFavor(userId, favorId, auth)
        statusFavorResponse.postValue(handleFavorQuery<StatusFavorResponse>(response))
    }
    public fun checkFavor(userId: String, favorId : String, auth: String) = viewModelScope.launch {
        statusFavorResponse.postValue(Resource.Loading())
        val response = favorRepository.checkfavor(userId, favorId, auth)
        statusFavorResponse.postValue(handleFavorQuery<StatusFavorResponse>(response))
    }
    public fun getFavor(userId: String, auth: String) = viewModelScope.launch {
        getFavorResponse.postValue(Resource.Loading())
        val response = favorRepository.getFavorHotel(userId, auth)
        getFavorResponse.postValue(handleFavorQuery<GetFavorResponse>(response))
    }
    private fun<T>handleFavorQuery(response : Response<T>) : Resource<T>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
}