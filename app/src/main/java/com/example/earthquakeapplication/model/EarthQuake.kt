package com.example.earthquakeapplication.model

import android.location.Location
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

data class EarthQuake(
    val mId: String,
    val mDate: Date,
    val mDetails: String,
    val mLocation: Location?,
    val mMagnitude: Double,
    val mLink: String?
) {
    override fun toString(): String {
        val simpleDateFormat = SimpleDateFormat("HH.mm", Locale.US)
        val dateString = simpleDateFormat.format(mDate)
        return "$dateString: $mMagnitude $mDetails"
    }

    override fun equals(other: Any?) = if (other is EarthQuake) {
        other.mId == this.mId
    } else {
        false
    }
}