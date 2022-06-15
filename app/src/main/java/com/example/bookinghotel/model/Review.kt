package com.example.bookinghotel.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(
    val name: String? = null,
    val review: String? = null,
    val Date: String? = null,
    val star: String? = null,
) : Parcelable