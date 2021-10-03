package com.sam.gogoeat.data.place

import com.sam.gogoeat.MyApplication
import com.sam.gogoeat.R

data class PlaceReq (
    var location: String? = null,
    var radius: Int? = null,
    var type: String? = null,
    var keyword: String? = null,
    var key: String? = null,
    var opennow: Boolean? = null,
    var pageToken: String? = null
) {
    companion object {
        private const val TYPE_EAT = "restaurant|food"

        fun create(lat: Double, lng: Double, radius: Int? = 1500, keyword: String? = null, opennow: Boolean? = null, pageToken: String? = null) : PlaceReq {
            val locationNow = "$lat,$lng"
            return PlaceReq(
                location = locationNow,
                radius = radius,
                type = TYPE_EAT,
                keyword = keyword,
                key = MyApplication.appContext.getString(R.string.map_key),
                opennow = opennow,
                pageToken = pageToken
            )
        }
    }
}