package com.sam.gogoeat.data.place

import com.sam.gogoeat.BuildConfig
import com.sam.gogoeat.utils.KeyUtil
import com.sam.gogoeat.utils.UserManager

data class PlaceReq (
    var location: String? = null,
    var radius: Int? = null,
    var type: String? = null,
    var keyword: String? = null,
    var key: String? = null,
    var opennow: Boolean? = null,
    var minprice: Int?,
    var maxprice: Int?,
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
                    key = KeyUtil.decodeKey(BuildConfig.MAP_API_KEY),
                    opennow = if (isOpen) isOpen else null,
                    minprice = priceLevel.getPrice(true),
                    maxprice = priceLevel.getPrice(false),
                    pageToken = null
                )
            }
        }

        private fun Int.getPrice(isMin: Boolean) : Int? {
            if (isMin) {
                when(this) {
                    1 -> return 0
                    2 -> return 2
                }
            } else {
                when(this) {
                    1 -> return 2
                    2 -> return 4
                }
            }
            return null
        }
    }
}