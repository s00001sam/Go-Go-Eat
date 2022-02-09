package com.sam.gogoeat.data

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.sam.gogoeat.view.support.PriceLevel
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingData(
    var myLocation : LatLng = LatLng(24.0, 121.0),
    var keyWord: String? = null,
    var distance: Int = 1000,
    var priceLevel: Int = PriceLevel.NONE.ordinal,
    var isOpen: Boolean = true,
    var onlyFindRestaurant: Boolean = true
): Parcelable