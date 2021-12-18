package com.sam.gogoeat.view

import android.os.Handler
import android.os.Looper
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

    private val _nearbyFoodResult = MutableStateFlow<Resource<List<PlaceData>>>(Resource.nothing(null))
    val nearbyFoodResult : StateFlow<Resource<List<PlaceData>>> = _nearbyFoodResult

    private val _listClick = MutableStateFlow<Boolean>(false)
    val listClick: StateFlow<Boolean> = _listClick

    private val _historyList = MutableStateFlow<List<PlaceData>>(listOf())
    val historyList : StateFlow<List<PlaceData>> = _historyList

    private val _newHistoryItem = MutableStateFlow<PlaceData?>(null)
    val newHistoryItem : StateFlow<PlaceData?> = _newHistoryItem

    val savedFoodResult = mutableListOf<PlaceData>()
    var saveToken = ""
    var firstGetLocation = false
    private var isListOpen = false

    fun setHistoryList(list: List<PlaceData>) {
        _historyList.value = list
    }

    fun setListClick() {
        isListOpen = !isListOpen
        _listClick.value = isListOpen
    }

    fun getNearbyFoods() {
        UserManager.myLocation.let { location ->
            val req = PlaceReq.create(location.latitude, location.longitude)
            viewModelScope.launch {
                getNearbyFoodsData.getFlow(req).collect {
                    _nearbyFoodResult.value = it
                    if (it.isSuccess() && it.hasNextPage() && !it.data.isNullOrEmpty()) {
                        it.nextPageToken?.let { token ->
                            savedFoodResult.clear()
                            savedFoodResult.addAll(it.data)
                            saveToken = token
                            Handler(Looper.getMainLooper()).postDelayed({ getMorePageFoods() }, 2000)
                        }
                    }
                }
            }
        }
    }

    fun getMorePageFoods() {
        UserManager.myLocation.let { location ->
            val req = PlaceReq.create(location.latitude, location.longitude, pageToken = saveToken)
            viewModelScope.launch {
                getNearbyFoodsData.getFlow(req).collect {
                    if (it.isFinished()) {
                        if (!it.data.isNullOrEmpty()) {
                            savedFoodResult.addAll(it.data)
                        }
                        if (it.hasNextPage()) {
                            it.nextPageToken?.let { token ->
                                saveToken = token
                                Handler(Looper.getMainLooper()).postDelayed({ getMorePageFoods() }, 2000)
                            }
                        } else {
                            if (savedFoodResult.isNotEmpty()) {
                                _nearbyFoodResult.value = Resource.success(savedFoodResult)
                                saveToken = ""
                            }
                        }
                    }
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