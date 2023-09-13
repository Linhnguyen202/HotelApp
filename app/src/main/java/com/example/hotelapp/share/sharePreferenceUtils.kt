package com.example.hotelapp.share

import android.content.Context
import android.content.SharedPreferences
import com.example.hotelapp.model.User
import com.google.gson.Gson

object sharePreferenceUtils {

    fun isSharedPreferencesExist(context: Context, sharedPreferencesName: String,key: String): Boolean {
        val sharedPreferences = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        return sharedPreferences.contains(key)
    }
    fun saveLanguage(lan : String = "eng", context: Context){
        val preferences =  context.getSharedPreferences("LAN",Context.MODE_PRIVATE)
        preferences.edit().putString("LAN_VALUE",lan).apply()
    }

    fun getLanguage(context: Context) : String? {
        val preferences =  context.getSharedPreferences("LAN",Context.MODE_PRIVATE)
        return preferences.getString("LAN_VALUE","")
    }

    fun saveToken(token: String, context: Context) {
        val preferences =  context.getSharedPreferences("USER",Context.MODE_PRIVATE)
        preferences.edit().putString("TOKEN_VALUE",token).apply()
    }

    fun getToken(context: Context) : String? {
        val preferences =  context.getSharedPreferences("USER",Context.MODE_PRIVATE)
        return preferences.getString("TOKEN_VALUE","")
    }
    fun saveUser(user: User,context: Context) {
        val preferences =  context.getSharedPreferences("USER",Context.MODE_PRIVATE)
        val gson = Gson()
        val user = gson.toJson(user)
        preferences.edit().putString("USER_VALUE",user).apply()
    }
    fun getUser(context: Context): User {
        val preferences =  context.getSharedPreferences("USER",Context.MODE_PRIVATE)
        val gson = Gson()
        val user = preferences.getString("USER_VALUE", null)
        return gson.fromJson(user,User::class.java)
    }

    fun removeUser(context: Context) {
        val sharedPref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.remove("USER_VALUE")
        editor.remove("TOKEN_VALUE")
        editor.apply()
    }

    fun saveDarkMode(lan : Boolean = false, context: Context){
        val preferences =  context.getSharedPreferences("DARK_MODE",Context.MODE_PRIVATE)
        preferences.edit().putBoolean("DARK",lan).apply()
    }

    fun getDarkMode(context: Context) : Boolean? {
        val preferences =  context.getSharedPreferences("DARK_MODE",Context.MODE_PRIVATE)
        return preferences.getBoolean("DARK",false)
    }


}