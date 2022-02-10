package com.sam.gogoeat.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.sam.gogoeat.api.resp.base.Resource
import com.sam.gogoeat.api.usecase.GetMyLocation
import com.sam.gogoeat.api.usecase.GetNearbyFoodsData
import com.sam.gogoeat.data.GogoPlace
import com.sam.gogoeat.data.place.PlaceData
import com.sam.gogoeat.data.place.PlaceData.Companion.toGogoPlace
import com.sam.gogoeat.data.place.PlaceReq
import com.sam.gogoeat.utils.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getNearbyFoodsData: GetNearbyFoodsData,
    private val getMyLocation: GetMyLocation
) : ViewModel() {

    //API response list
    private val _nearbyFoodResult = MutableStateFlow<Resource<List<PlaceData>>>(Resource.nothing())
    val nearbyFoodResult : StateFlow<Resource<List<PlaceData>>> = _nearbyFoodResult

    //temp saved history list
    private val _historyList = MutableStateFlow<List<GogoPlace>>(listOf())
    val historyList : StateFlow<List<GogoPlace>> = _historyList

    //the newest lottery item
    private val _newHistoryItem = MutableStateFlow<GogoPlace?>(null)
    val newHistoryItem : StateFlow<GogoPlace?> = _newHistoryItem

    private val _locationResult = MutableStateFlow<Resource<LatLng>>(Resource.nothing())
    val locationResult : StateFlow<Resource<LatLng>> = _locationResult

    var firstGetLocation = false

    fun newHistoryShowFinish() {
        _newHistoryItem.value = null
    }

    fun getNearbyFoods() {
        viewModelScope.launch {
            getNearbyFoodsData.getFlow(PlaceReq.create()).collect {
                _nearbyFoodResult.value = it
            }
        }
    }

    fun getRandomFoodIntoHistory() {
        nearbyFoodResult.value.data?.let {
            if (it.isEmpty()) return
            val list = mutableListOf<GogoPlace>()
            val newItem = it[Util.getRandomNum(it.size)].toGogoPlace()
            list.addAll(historyList.value)
            list.add(0, newItem)
            _newHistoryItem.value = newItem
            _historyList.value = list
        }
    }

    fun getLocation(client: FusedLocationProviderClient) {
        viewModelScope.launch {
            getMyLocation.getFlow(client).collect {
                _locationResult.value = it
            }
        }
    }

}