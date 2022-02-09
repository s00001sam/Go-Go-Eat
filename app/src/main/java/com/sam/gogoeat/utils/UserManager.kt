package com.sam.gogoeat.utils

import com.sam.gogoeat.data.SettingData

object UserManager {
    val PRICE_STR_LIST = listOf("不限制", "低價位", "高價位")

    var mySettingData = SettingData()

    fun getPriceStr(i: Int) = PRICE_STR_LIST[i]

    fun getMyPriceStr() = PRICE_STR_LIST[mySettingData.priceLevel]
}