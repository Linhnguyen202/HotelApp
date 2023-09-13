package com.example.hotelapp.viewModel.AuthViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.hotelapp.model.SignInBody
import com.example.hotelapp.model.User
import com.example.hotelapp.model.UserResponse
import com.example.hotelapp.model.registerBody
import com.example.hotelapp.repository.AuthRepository
import com.example.hotelapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel (val app: Application, val authRepository: AuthRepository): ViewModel() {
    val user : MutableLiveData<Resource<UserResponse>> = MutableLiveData()
    val userUdapteProfile : MutableLiveData<Resource<UserResponse>> = MutableLiveData()

    public fun updateUserProfile(user : User, userId: String) = viewModelScope.launch {
        userUdapteProfile.postValue(Resource.Loading())
        val response = authRepository.updateUser(userId, user)
        userUdapteProfile.postValue(handleUserResponse(response))
    }
    public fun loginUser(signInBody: SignInBody)  = liveData(Dispatchers.IO)  {
        emit(Resource.Loading())
        try{
            emit(Resource.Success(authRepository.signInUser(signInBody)))
        }
        catch(e:Exception){
            emit(Resource.Error("error"))
        }

    }
    public fun resetUser(){

    }
    private fun registerUSer(registerBody: registerBody) = viewModelScope.launch {
        user.postValue(Resource.Loading())
        val response = authRepository.registerUser(registerBody)
        user.postValue(handleUserResponse(response))
    }
    private fun handleUserResponse(response: Response<UserResponse>) : Resource<UserResponse>{
       if(response.isSuccessful){
           response.body()?.let {
               return Resource.Success(it)
           }
       }
        return Resource.Error(response.code().toString())
    }
    public fun makeLogin(signInBody: SignInBody){
        loginUser(signInBody)
    }
    public fun makeRegister(registerBody: registerBody){
        registerUSer(registerBody)
    }
}