package com.example.hotelapp.model

import android.os.Parcel
import android.os.Parcelable

data class Booking(
    val _id: String,
    val name: String?,
    val totalPrice: Int?,
    val price: Int?,
    val status: String?,
    val numberRoom: Int?,
    val guestNumber: Int?,
    val paymentType: String?,
    val user: User,
    val hotel: Hotel,
    val room: Room,
    val createdAt: String?,
    val __v: Int,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readParcelable<User>(User::class.java.classLoader)!!,
        parcel.readParcelable<Hotel>(Hotel::class.java.classLoader)!!,
        parcel.readParcelable<Room>(Room::class.java.classLoader)!!,
        parcel.readString(),
        parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(name)
        parcel.writeInt(totalPrice!!)
        parcel.writeInt(price!!)
        parcel.writeString(status)
        parcel.writeInt(numberRoom!!)
        parcel.writeInt(guestNumber!!)
        parcel.writeString(paymentType)
        parcel.writeParcelable(user,flags)
        parcel.writeParcelable(hotel,flags)
        parcel.writeParcelable(room,flags)
        parcel.writeString(createdAt)
        parcel.writeInt(__v)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Booking> {
        override fun createFromParcel(parcel: Parcel): Booking {
            return Booking(parcel)
        }

        override fun newArray(size: Int): Array<Booking?> {
            return arrayOfNulls(size)
        }
    }
}