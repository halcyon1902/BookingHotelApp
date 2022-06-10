package com.example.bookinghotel.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp

@Parcelize
data class Review(
    val name: String? = null,
    val review: String? = null,
    val Date: Timestamp? = null,
    val totalStarGiven: Double? = null,
) : Parcelable
