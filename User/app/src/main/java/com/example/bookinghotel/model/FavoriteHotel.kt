package com.example.bookinghotel.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteHotel(
    val description: String? = null,
    val roomnumber: String? = null,
    val image: String? = null,
) : Parcelable
