package com.sam.gogoeat.data.place

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    val lat: Double,
    val lng: Double
) : Parcelable