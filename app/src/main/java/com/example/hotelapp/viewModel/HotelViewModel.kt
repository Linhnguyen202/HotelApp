package com.example.hotelapp.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelapp.model.HotelResponse
import com.example.hotelapp.model.Search
import com.example.hotelapp.repository.AuthRepository
import com.example.hotelapp.repository.HotelRepository
import com.example.hotelapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class HotelViewModel(val app: Application, val hotelRepository: HotelRepository): ViewModel() {
    val hotelList : MutableLiveData<Resource<HotelResponse>> = MutableLiveData();
    val popularHotelList : MutableLiveData<Resource<HotelResponse>> = MutableLiveData()
    val trendingHotelList : MutableLiveData<Resource<HotelResponse>> = MutableLiveData()
    val hotelListSearching : MutableLiveData<Resource<HotelResponse>> = MutableLiveData();
    public var DATA_TYPE : String? = "ALL";
    var searchData : MutableLiveData<Search> = MutableLiveData()
    public fun setDataSearch(searchData: Search){
        this.searchData.postValue(searchData)
    }
    public fun setDataType(type:String){
        this.DATA_TYPE = type;
        when(DATA_TYPE){
            "ALL"->{
                getHotelData()
            }
            "New"->{
                getTrendingHotel(DATA_TYPE!!)
            }
            "Popular"->{
                getPopularHotel(DATA_TYPE!!)
            }
        }

    }
    private fun getHotelData() = viewModelScope.launch(Dispatchers.IO) {
        hotelList.postValue(Resource.Loading())
        val response = hotelRepository.getHotelData(1,10);
        hotelList.postValue(handleHotelData(response))
    }
    private fun getPopularHotel(type: String) = viewModelScope.launch {
        popularHotelList.postValue(Resource.Loading())
        val response = hotelRepository.getTypeHotel(type,1,10)
        popularHotelList.postValue(handleHotelData(response))
    }

    private fun getTrendingHotel(type: String) = viewModelScope.launch {
        trendingHotelList.postValue(Resource.Loading())
        val response = hotelRepository.getTypeHotel(type,1,10)
        trendingHotelList.postValue(handleHotelData(response))
    }

   public fun getSearching(cityQuery: String, guestNumber: Int) = viewModelScope.launch {
       hotelListSearching.postValue(Resource.Loading())
       val response = hotelRepository.searchHotel(cityQuery,guestNumber)
       hotelListSearching.postValue(handleHotelData(response))
   }



    private fun handleHotelData(response: Response<HotelResponse>) : Resource<HotelResponse>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it);
            }
        }
       return Resource.Error(response.message())
    }
}