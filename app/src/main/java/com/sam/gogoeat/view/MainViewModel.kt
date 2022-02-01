package com.sam.gogoeat.view

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.gogoeat.api.resp.base.Resource
import com.sam.gogoeat.api.usecase.GetNearbyFoodsData
import com.sam.gogoeat.data.place.PlaceData
import com.sam.gogoeat.data.place.PlaceReq
import com.sam.gogoeat.utils.UserManager
import com.sam.gogoeat.utils.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val getNearbyFoodsData: GetNearbyFoodsData) : ViewModel() {

    //API response list
    private val _nearbyFoodResult = MutableStateFlow<Resource<List<PlaceData>>>(Resource.nothing())
    val nearbyFoodResult : StateFlow<Resource<List<PlaceData>>> = _nearbyFoodResult

    //temp saved history list
    private val _historyList = MutableStateFlow<List<PlaceData>>(listOf())
    val historyList : StateFlow<List<PlaceData>> = _historyList

    //the newest lottery item
    private val _newHistoryItem = MutableStateFlow<PlaceData?>(null)
    val newHistoryItem : StateFlow<PlaceData?> = _newHistoryItem
    var firstGetLocation = false

    fun getNearbyFoods() {
        UserManager.myLocation.let { location ->
            val req = PlaceReq.create(location.latitude, location.longitude)
            viewModelScope.launch {
                getNearbyFoodsData.getFlow(req).collect {
                    _nearbyFoodResult.value = it
                }
            }
        }
    }

    fun getRandomFoodIntoHistory() {
        nearbyFoodResult.value.data?.let {
            if (it.isEmpty()) return
            val list = mutableListOf<PlaceData>()
            val newItem = it[Util.getRandomNum(it.size)]
            list.addAll(historyList.value)
            list.add(0, newItem)
            _newHistoryItem.value = newItem
            _historyList.value = list
        }
    }

}