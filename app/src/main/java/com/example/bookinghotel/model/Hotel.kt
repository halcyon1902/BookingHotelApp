package com.example.bookinghotel.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hotel(
    val floor: String? = null,
    val mota: String? = null,
    val price: String? = null,
    val roomnumber: String? = null,
    val typeroom: String? = null,
    val image1: String? = null,
    val image2: String? = null,
    val image3: String? = null,
    val image4: String? = null,
) : Parcelable

