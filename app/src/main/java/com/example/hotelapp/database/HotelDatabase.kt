package com.example.hotelapp.database

import android.content.Context
import androidx.room.*
import com.example.hotelapp.model.Hotel

@Database(entities = [Hotel::class], version = 1)
abstract class HotelDatabase : RoomDatabase(){
    abstract fun getHotelDao(): HotelDao
    companion object {
        @Volatile
        private var instance : HotelDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                HotelDatabase::class.java,
                "hotel_db"
            ).build()
    }
}