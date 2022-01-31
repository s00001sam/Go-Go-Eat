package com.sam.gogoeat.data.place

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OpeningHours(
    val open_now: Boolean? = null
) : Parcelable