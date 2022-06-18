package com.example.bookinghotel.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class Booking(
    val datecome: String? = null,
    val dateleave: String? = null,
    val typeroom: String? = null,
    val email: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val staydate: String? = null,
    val currentdate: String? = null,
    val amount: String? = null,
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
        parcel.readString())

    companion object : Parceler<Booking> {

        override fun Booking.write(parcel: Parcel, flags: Int) {
            parcel.writeString(datecome)
            parcel.writeString(dateleave)
            parcel.writeString(typeroom)
            parcel.writeString(email)
            parcel.writeString(name)
            parcel.writeString(phone)
            parcel.writeString(staydate)
            parcel.writeString(currentdate)
            parcel.writeString(amount)
        }

        override fun create(parcel: Parcel): Booking {
            return Booking(parcel)
        }
    }
}