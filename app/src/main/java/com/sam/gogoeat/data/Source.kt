package com.sam.gogoeat.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Source(
    val id: String? = null,
    val name: String? = null
): Parcelable