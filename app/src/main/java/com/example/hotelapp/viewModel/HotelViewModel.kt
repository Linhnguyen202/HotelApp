package com.example.hotelapp.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.hotelapp.application.HotelApplication
import com.example.hotelapp.model.Hotel
import com.example.hotelapp.model.HotelResponse
import com.example.hotelapp.model.Search
import com.example.hotelapp.paging.HotelPagingSource
import com.example.hotelapp.paging.HotelSearchingSource
import com.example.hotelapp.paging.TypeHotelPagingSource
import com.example.hotelapp.repository.AuthRepository
import com.example.hotelapp.repository.HotelRepository
import com.example.hotelapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import kotlin.reflect.typeOf

class HotelViewModel(val app: Application, val hotelRepository: HotelRepository): AndroidViewModel(app) {

    val popularHotelList : MutableLiveData<Resource<HotelResponse>> = MutableLiveData()
    val trendingHotelList : MutableLiveData<Resource<HotelResponse>> = MutableLiveData()
    val hotelListSearching : MutableLiveData<Resource<HotelResponse>> = MutableLiveData();
    public var DATA_TYPE : String? = "ALL";
    var searchData : MutableLiveData<Search> = MutableLiveData()
    public fun setDataSearch(searchData: Search){
        this.searchData.postValue(searchData)
    }





    val hotelListPage : Flow<PagingData<Hotel>> = Pager(PagingConfig(1)){
        HotelPagingSource(hotelRepository)
    }.flow.cachedIn(viewModelScope)

    val hotelTypePage = Pager(PagingConfig(1)){
        TypeHotelPagingSource(hotelRepository,this@HotelViewModel.DATA_TYPE!!)
    }.flow.cachedIn(viewModelScope)



    val hotelList : MutableLiveData<Resource<HotelResponse>> = MutableLiveData();

    val hotelListSearch : MutableLiveData<PagingData<Hotel>> = MutableLiveData();

    public suspend fun getSearching(cityQuery: String?, guestNumber: Int) : Flow<PagingData<Hotel>> {
      if(cityQuery.equals("null")) {
//         return Pager(PagingConfig(1)) {
//              HotelPagingSource(hotelRepository)
//          }.flow.cachedIn(viewModelScope)
          return Pager(PagingConfig(1)){
              HotelSearchingSource(hotelRepository, "hanoi", 3)
          }.flow.cachedIn(viewModelScope)
      }
      else{
          return Pager(PagingConfig(1)){
              HotelSearchingSource(hotelRepository, cityQuery.toString(), guestNumber)
          }.flow.cachedIn(viewModelScope)
      }

    }





    private fun handleHotelData(response: Response<HotelResponse>) : Resource<HotelResponse>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it);
            }
        }
       return Resource.Error(response.message())
    }



    // testing internet
    fun getTypeHotel(type: String) = viewModelScope.launch {
        when(type){
            "Popular" -> {
                safeHotelCall(type)
            }
            "New" -> {
                getTrendingHotel(type)
            }
            "ALL" -> {
                getHotelData()
            }
        }

    }
    private suspend fun getHotelData() = viewModelScope.launch(Dispatchers.IO) {
        hotelList.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()){
//                val response = hotelRepository.getHotelData(1,10)
//                hotelList.postValue(handleHotelData(response))

            }
            else{
                hotelList.postValue(Resource.Error("No internet connection"))
            }
        }
        catch (err: Throwable){
            when(err){
                is IOException -> hotelList.postValue(Resource.Error("Networl failure"))
                else -> hotelList.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
    private suspend fun getTrendingHotel(type: String) {
        trendingHotelList.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()){
                val response = hotelRepository.getTypeHotel(type,1,10)
                trendingHotelList.postValue(handleHotelData(response))
            }
            else{
                trendingHotelList.postValue(Resource.Error("No internet connection"))
            }
        }
        catch (err: Throwable){
            when(err){
                is IOException -> trendingHotelList.postValue(Resource.Error("Networl failure"))
                else -> trendingHotelList.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    private suspend fun safeHotelCall(type: String) {
        popularHotelList.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = hotelRepository.getTypeHotel(type,1,10)
                popularHotelList.postValue(handleHotelData(response))
            }
            else{
                popularHotelList.postValue(Resource.Error("No internet connection"))
            }
        }
        catch (err: Throwable){
            when(err){
                is IOException -> popularHotelList.postValue(Resource.Error("Networl failure"))
                else -> popularHotelList.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun hasInternetConnection() : Boolean {
        val connectivityManager : ConnectivityManager = getApplication<HotelApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        // handle <25
        else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}