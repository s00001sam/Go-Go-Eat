package com.sam.gogoeat.data.place

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Viewport(
    val northeast: Northeast,
    val southwest: Southwest
) : Parcelable