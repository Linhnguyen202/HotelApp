package com.example.hotelapp.model

import android.os.Parcel
import android.os.Parcelable

data class Room(
    val __v: Int,
    val _id: String,
    val createdAt: String?,
    val description: String?,
    val hotel: String?,
    val name: String?,
    val numberRoom: Int,
    val price: Int,
    val priceDiscount: Int,
    val rate: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(__v)
        parcel.writeString(_id)
        parcel.writeString(createdAt)
        parcel.writeString(description)
        parcel.writeString(hotel)
        parcel.writeString(name)
        parcel.writeInt(numberRoom)
        parcel.writeInt(priceDiscount)
        parcel.writeInt(rate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Room> {
        override fun createFromParcel(parcel: Parcel): Room {
            return Room(parcel)
        }

        override fun newArray(size: Int): Array<Room?> {
            return arrayOfNulls(size)
        }
    }
}