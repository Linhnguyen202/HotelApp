package com.example.hotelapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.hotelapp.model.Hotel
import com.example.hotelapp.repository.HotelRepository
import retrofit2.HttpException

class HotelSearchingSource(private val repository: HotelRepository, private val city: String, private val guestNumber : Int) : PagingSource<Int, Hotel>(){
    override fun getRefreshKey(state: PagingState<Int, Hotel>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hotel> {
        return try{
            val currentPage = params.key ?: 1
            val response = repository.searchHotel(city, guestNumber, currentPage, 10)
            val data = response.body()!!.result
            val responseData = mutableListOf<Hotel>()

            if(!data.isNullOrEmpty()) {
                responseData.addAll(data)
                LoadResult.Page(
                    data = responseData,
                    prevKey = if (currentPage == 1) null else -1,
                    nextKey = currentPage.plus(1)
                )
            } else {
                LoadResult.Page(
                    data = responseData,
                    prevKey = if (currentPage == 1) null else -1,
                    nextKey = null
                )
            }
        }catch (e : java.lang.Exception){
            LoadResult.Error(e)
        }catch (httpE : HttpException){
            LoadResult.Error(httpE)
        }
    }

}