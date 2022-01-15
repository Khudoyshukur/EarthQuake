package com.example.earthquakeapplication.model

import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Entity
data class EarthQuake(
    @PrimaryKey
    val mId: String = "",
    val mDate: Date = Calendar.getInstance().time,
    val mDetails: String = "",
    val mLocation: Location? = null,
    val mMagnitude: Double = -1.0,
    val mLink: String? = ""
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

    override fun hashCode(): Int {
        var result = mId.hashCode()
        result = 31 * result + mDate.hashCode()
        result = 31 * result + mDetails.hashCode()
        result = 31 * result + (mLocation?.hashCode() ?: 0)
        result = 31 * result + mMagnitude.hashCode()
        result = 31 * result + (mLink?.hashCode() ?: 0)
        return result
    }
}