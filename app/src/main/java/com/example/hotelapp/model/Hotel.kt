package com.example.hotelapp.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName ="Hotel"
)
data class Hotel(
    @PrimaryKey
    val _id: String,
    val address: String?,
    val city: String?,
    val createdAt: String?,
    val description: String?,
    val name: String?,
    val price: Int,
    val priceDiscount: Int,
    val rate: Float,
    val type: String?,
    val image: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readString(),
        parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(address)
        parcel.writeString(city)
        parcel.writeString(createdAt)
        parcel.writeString(description)
        parcel.writeString(name)
        parcel.writeInt(price)
        parcel.writeInt(priceDiscount)
        parcel.writeFloat(rate)
        parcel.writeString(type)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Hotel> {
        override fun createFromParcel(parcel: Parcel): Hotel {
            return Hotel(parcel)
        }

        override fun newArray(size: Int): Array<Hotel?> {
            return arrayOfNulls(size)
        }
    }
}