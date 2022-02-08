package com.sam.gogoeat.data

import com.google.android.gms.maps.model.LatLng

data class SettingData(
    var myLocation : LatLng = LatLng(24.0, 121.0),
    var keyWord: String? = null,
    var distance: Int = 1000,
    var maxPrice: Int? = null,
    var minPrice: Int? = null,
    var isOpen: Boolean = true,
    var onlyFindRestaurant: Boolean = true
)