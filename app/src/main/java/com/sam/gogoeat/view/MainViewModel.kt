package com.sam.gogoeat.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.sam.gogoeat.api.resp.base.State
import com.sam.gogoeat.api.usecase.GetMyLocation
import com.sam.gogoeat.api.usecase.GetNearbyFoodsData
import com.sam.gogoeat.api.usecase.InsertHistoryItem
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
    private val getMyLocation: GetMyLocation,
    private val insertHistoryItem: InsertHistoryItem
) : ViewModel() {

    //API response list
    private val _nearbyFoodResult = MutableStateFlow<State<List<PlaceData>>>(State.nothing())
    val nearbyFoodResult : StateFlow<State<List<PlaceData>>> = _nearbyFoodResult

    //the newest lottery item
    private val _newHistoryItem = MutableStateFlow<GogoPlace?>(null)
    val newHistoryItem : StateFlow<GogoPlace?> = _newHistoryItem

    private val _locationResult = MutableStateFlow<State<LatLng>>(State.nothing())
    val locationResult : StateFlow<State<LatLng>> = _locationResult

    private val _insertHistoryResult = MutableStateFlow<State<Long?>>(State.nothing())
    val insertHistoryResult : StateFlow<State<Long?>> = _insertHistoryResult

    var firstGetLocation = false

    fun newHistoryShowFinish() {
        _newHistoryItem.value = null
    }

    fun getNearbyFoods() {
        viewModelScope.launch {
            getNearbyFoodsData.getDataState(PlaceReq.create()).collect {
                _nearbyFoodResult.value = it
            }
        }
    }

    fun getRandomFoodIntoHistory() {
        nearbyFoodResult.value.data?.let {
            if (it.isEmpty()) return
            val newItem = it[Util.getRandomNum(it.size)].toGogoPlace()
            _newHistoryItem.value = newItem
            insertHistoryItem(newItem)
        }
    }

    fun insertHistoryItem(gogoPlace: GogoPlace) {
        viewModelScope.launch {
            insertHistoryItem.getDataState(gogoPlace).collect {
                _insertHistoryResult.value = it
            }
        }
    }

    fun completeInsertHistories() {
        _insertHistoryResult.value = State.nothing()
    }

    fun getLocation(client: FusedLocationProviderClient) {
        viewModelScope.launch {
            getMyLocation.getDataState(client).collect {
                _locationResult.value = it
            }
        }
    }

}