package com.example.hotelapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.hotelapp.model.Hotel
import com.example.hotelapp.repository.HotelRepository
import retrofit2.HttpException

class TypeHotelPagingSource(private val repository: HotelRepository, private val type : String) : PagingSource<Int, Hotel>() {
    override fun getRefreshKey(state: PagingState<Int, Hotel>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hotel> {
        return try{
            val current = params.key ?: 1
            val response = repository.getTypeHotel(type, current, 2)
            val data = response.body()!!.result
            val responseData = mutableListOf<Hotel>()
            if(!data.isNullOrEmpty()){
                responseData.addAll(data)
                LoadResult.Page(
                    data = responseData,
                    prevKey = if(current == 1) null else -1,
                    nextKey = current.plus(1)
                )
            }
            else{
                LoadResult.Page(
                    data = responseData,
                    prevKey = if (current == 1) null else -1,
                    nextKey = null
                )
            }
        }
        catch (e: Exception){
            LoadResult.Error(e)
        }
        catch (httpE: HttpException){
            LoadResult.Error(httpE)
        }
    }

}