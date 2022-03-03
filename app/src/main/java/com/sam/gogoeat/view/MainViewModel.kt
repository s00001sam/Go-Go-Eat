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
import com.sam.gogoeat.data.place.PlaceData.Companion.toGogoPlaces
import com.sam.gogoeat.data.place.PlaceReq
import com.sam.gogoeat.utils.Logger
import com.sam.gogoeat.utils.UserManager
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

    //real used list
    private val _nearbyFoods = MutableStateFlow<List<GogoPlace>>(listOf())
    val nearbyFoods : StateFlow<List<GogoPlace>> = _nearbyFoods

    //the newest lottery item
    private val _newHistoryItem = MutableStateFlow<GogoPlace?>(null)
    val newHistoryItem : StateFlow<GogoPlace?> = _newHistoryItem

    private val _locationResult = MutableStateFlow<State<LatLng>>(State.nothing())
    val locationResult : StateFlow<State<LatLng>> = _locationResult

    private val _insertHistoryResult = MutableStateFlow<State<Long?>>(State.nothing())
    val insertHistoryResult : StateFlow<State<Long?>> = _insertHistoryResult

    private var randomIndex: Int = 0
    private var firstGetLocation = false

    fun firstGetLocation() = firstGetLocation

    fun setFirstGetLocationOk() {
        firstGetLocation = true
    }

    fun newHistoryShowFinish() {
        _newHistoryItem.value = null
    }

    fun getNearbyFoods(dismissLoading:()-> Unit) {
        if (UserManager.isSameSetting()) { // 若條件相同且在上次位置附近則不打 API
            dismissLoading.invoke()
            return
        }
        viewModelScope.launch {
            getNearbyFoodsData.getDataState(PlaceReq.create()).collect {
                _nearbyFoodResult.value = it
            }
        }
    }

    fun setNearbyFoods(list: List<GogoPlace>) {
        _nearbyFoods.value = list
    }

    fun getNearByGogoPlaces() = nearbyFoods.value

    fun getRandomIndex() : Int {
        if (getNearByGogoPlaces().isNullOrEmpty()) return 0
        val index = Util.getRandomNum(getNearByGogoPlaces().size)
        this.randomIndex = index ?: 0
        return index ?: 0
    }

    fun getRandomFoodIntoHistory() {
        nearbyFoods.value.let {
            if (it.isEmpty()) return
            val newItem = it[randomIndex]
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