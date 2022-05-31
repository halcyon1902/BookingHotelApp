package com.example.bookinghotel

import android.os.Parcel
import android.os.Parcelable

data class Hotel(
    val floor: String? = null,
    val mota: String? = null,
    val price: String? = null,
    var roomnumber: String? = null,
    val typeroom: String? = null,
    val image1: String? = null,
    val image2: String? = null,
    val image3: String? = null,
    val image4: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(floor)
        parcel.writeString(mota)
        parcel.writeString(price)
        parcel.writeString(roomnumber)
        parcel.writeString(typeroom)
        parcel.writeString(image1)
        parcel.writeString(image2)
        parcel.writeString(image3)
        parcel.writeString(image4)
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
