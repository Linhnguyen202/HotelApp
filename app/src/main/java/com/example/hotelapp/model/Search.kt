package com.example.hotelapp.model

import android.os.Parcel
import android.os.Parcelable

data class Search(
    var place: String? = null,
    var time: String? = null,
    var room: String? = null,
    var adults: String? = null,
    var children: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(place)
        parcel.writeString(time)
        parcel.writeString(room)
        parcel.writeString(adults)
        parcel.writeString(children)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Search> {
        override fun createFromParcel(parcel: Parcel): Search {
            return Search(parcel)
        }

        override fun newArray(size: Int): Array<Search?> {
            return arrayOfNulls(size)
        }
    }
}