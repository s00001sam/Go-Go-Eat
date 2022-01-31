package com.sam.gogoeat.data.place

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Photo(
    val height: Int,
    val html_attributions: @RawValue List<String>,
    val photo_reference: String,
    val width: Int
) : Parcelable