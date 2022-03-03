package com.sam.gogoeat.utils

import com.google.android.gms.maps.model.LatLng
import com.sam.gogoeat.data.SettingData
import com.sam.gogoeat.utils.Util.getDinstance

object UserManager {

    private const val USER_SETTING = "USER_SETTING"

    val PRICE_STR_LIST = listOf("不限制", "低價位", "高價位")

    var preSettingData: SettingData = SettingData()

    var mySettingData: SettingData = SettingData()

    fun makePreSameAsMySetting() {
        preSettingData = mySettingData.copy()
    }

    fun setMyLocation(location: LatLng) {
        mySettingData.myLocation = location
        saveSpSetting()
    }

    fun getSpSetting() {
        mySettingData = SpUtil.getObject(SettingData::class.java, USER_SETTING) ?: SettingData()
        makePreSameAsMySetting()
    }

    fun saveSpSetting() {
        SpUtil.setObject(SettingData::class.java, USER_SETTING, mySettingData)
    }

    fun getPriceStr(i: Int) = PRICE_STR_LIST[i]

    fun getMyPriceStr() = PRICE_STR_LIST[mySettingData.priceLevel]

    fun isSameSetting(): Boolean {
        val mockPre = preSettingData.copy(myLocation = LatLng(1.0, 1.0))
        val mockMy = mySettingData.copy(myLocation = LatLng(1.0, 1.0))
        Logger.d("sam00 mockPre=${mockPre} mockMy=$mockMy")
        return mockMy == mockPre && mySettingData.myLocation.getDinstance(preSettingData.myLocation) < 500
    }
}