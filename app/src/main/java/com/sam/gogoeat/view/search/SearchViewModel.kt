package com.sam.gogoeat.view.search

import androidx.lifecycle.ViewModel
import com.sam.gogoeat.utils.UserManager
import com.sam.gogoeat.view.support.PriceLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    val keyFocus = MutableStateFlow<Boolean>(false)
    val priceFocus = MutableStateFlow<Boolean>(false)

    val keyWordStr = MutableStateFlow<String>("")
    val onlyOpen = MutableStateFlow<Boolean>(true)
    val onlyRestaurant = MutableStateFlow<Boolean>(true)

    val priceStr = MutableStateFlow<String>("")
    var priceLevelNum: Int = PriceLevel.NONE.ordinal

    private val _distanceValue = MutableStateFlow<Int>(1000)
    val distanceValue: StateFlow<Int> = _distanceValue

    init {
        setCurrentValues()
    }

    private fun setCurrentValues() {
        UserManager.mySettingData.run {
            keyWordStr.value = keyWord ?: ""
            _distanceValue.value = distance
            priceStr.value = UserManager.PRICE_STR_LIST[priceLevel]
            priceLevelNum = priceLevel
            onlyOpen.value = isOpen
            onlyRestaurant.value = onlyFindRestaurant
        }
    }

    fun setDistance(d: Int) {
        _distanceValue.value = d
    }

    fun setPrice(priceLevel: Int, price: String) {
        priceStr.value = price
        priceLevelNum = priceLevel
    }

    fun setPriceFocus(focus: Boolean) {
        priceFocus.value = focus
    }

    fun resetData() {
        keyWordStr.value = ""
        priceLevelNum = PriceLevel.NONE.ordinal
        priceStr.value = UserManager.PRICE_STR_LIST[priceLevelNum]
        onlyOpen.value = true
        onlyRestaurant.value = true
    }

    fun setData2UserManager() {
        UserManager.mySettingData.apply {
            keyWord = keyWordStr.value
            distance = distanceValue.value
            priceLevel = priceLevelNum
            isOpen = onlyOpen.value
            onlyFindRestaurant = onlyRestaurant.value
        }
        UserManager.saveSpSetting()
    }

}