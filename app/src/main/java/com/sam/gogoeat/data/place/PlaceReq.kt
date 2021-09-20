package com.sam.gogoeat.data.place

import com.sam.gogoeat.MyApplication
import com.sam.gogoeat.R

data class PlaceReq (
    var location: String? = null,
    var radius: Int? = null,
    var type: String? = null,
    var keyword: String? = null,
    var key: String? = null
) {
    companion object {
        private const val FOOD_TYPE = "food"

        fun create(lat: Double, lng: Double, radius: Int? = 1500, keyword: String? = null) : PlaceReq {
            val locationNow = "$lat,$lng"
            return PlaceReq(
                location = locationNow,
                radius = radius,
                type = FOOD_TYPE,
                keyword = keyword,
                key = MyApplication.appContext.getString(R.string.map_key)
            )
        }
    }
}