package com.sam.gogoeat.data.place

import com.sam.gogoeat.MyApplication
import com.sam.gogoeat.R
import com.sam.gogoeat.utils.UserManager

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

        fun create() : PlaceReq {
            UserManager.mySettingData.run {
                val locationNow = "${myLocation.latitude},${myLocation.longitude}"
                return PlaceReq(
                    location = locationNow,
                    radius = distance,
                    type = if (onlyFindRestaurant) TYPE_EAT else null,
                    keyword = keyWord,
                    key = MyApplication.appContext.getString(R.string.map_key),
                    opennow = if (isOpen) isOpen else null,
                    pageToken = null
                )
            }
        }
    }
}