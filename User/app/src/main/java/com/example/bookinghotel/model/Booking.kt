package com.example.bookinghotel.model

import android.os.Parcelable
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
    var status: Boolean? = null,
    val currentdate: String? = null,
    val amount: String? = null,
) : Parcelable