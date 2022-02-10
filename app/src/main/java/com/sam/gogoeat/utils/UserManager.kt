package com.sam.gogoeat.utils

import android.content.Context
import com.sam.gogoeat.MyApplication
import com.sam.gogoeat.data.SettingData

object UserManager {

    private const val USER_SETTING = "USER_SETTING"

    val PRICE_STR_LIST = listOf("不限制", "低價位", "高價位")

    var mySettingData: SettingData = SettingData()

    fun getSpSetting() {
        mySettingData = SpUtil.getObject(SettingData::class.java, USER_SETTING) ?: SettingData()
    }

    fun saveSpSetting() {
        SpUtil.setObject(SettingData::class.java, USER_SETTING, mySettingData)
    }

    fun getPriceStr(i: Int) = PRICE_STR_LIST[i]

    fun getMyPriceStr() = PRICE_STR_LIST[mySettingData.priceLevel]
}