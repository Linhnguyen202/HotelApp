package com.example.hotelapp.model

import android.os.Parcel
import android.os.Parcelable

data class User(
    val __v: Int,
    val _id: String,
    val address: String?,
    val identification : String?,
    val email: String?,
    val password: String?,
    val phoneNumber: String?,
    val username: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(__v)
        parcel.writeString(_id)
        parcel.writeString(address)
        parcel.writeString(identification)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(phoneNumber)
        parcel.writeString(username)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}