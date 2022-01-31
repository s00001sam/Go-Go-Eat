package com.sam.gogoeat.data.place

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Southwest(
    val lat: Double,
    val lng: Double
) : Parcelable