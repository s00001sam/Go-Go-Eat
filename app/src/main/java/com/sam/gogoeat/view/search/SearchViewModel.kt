package com.sam.gogoeat.view.search

import androidx.lifecycle.ViewModel
import com.sam.gogoeat.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    val keyFocus = MutableStateFlow<Boolean>(false)
    val keyWordStr = MutableStateFlow<String>("")
    val onlyOpen = MutableStateFlow<Boolean>(false)
    val onlyRestaurant = MutableStateFlow<Boolean>(true)

    private val _distanceValue = MutableStateFlow<Int>(1000)
    val distanceValue: StateFlow<Int> = _distanceValue

    init {
        setCurrentValues()
    }

    private fun setCurrentValues() {
        UserManager.mySettingData.run {
            keyWordStr.value = keyWord ?: ""
            _distanceValue.value = distance
            onlyOpen.value = isOpen
            onlyRestaurant.value = onlyFindRestaurant
        }
    }

    fun setDistance(d: Int) {
        _distanceValue.value = d
    }

    fun resetData() {
        keyWordStr.value = ""
        onlyOpen.value = false
        onlyRestaurant.value = true
    }

    fun setData2UserManager() { //todo 新增價位
        UserManager.mySettingData.apply {
            keyWord = keyWordStr.value
            distance = distanceValue.value
            isOpen = onlyOpen.value
            onlyFindRestaurant = onlyRestaurant.value
        }
    }

}