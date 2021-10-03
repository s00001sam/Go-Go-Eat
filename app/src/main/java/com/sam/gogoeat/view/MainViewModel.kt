package com.sam.gogoeat.view

import android.os.Handler
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.gogoeat.api.resp.base.Resource
import com.sam.gogoeat.api.usecase.GetNearbyFoodsData
import com.sam.gogoeat.data.place.PlaceData
import com.sam.gogoeat.data.place.PlaceReq
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

    val savedFoodResult = mutableListOf<PlaceData>()

    var saveToken = ""

    fun getNearbyFoods() {
        val req = PlaceReq.create(24.991488, 121.511536)
        viewModelScope.launch {
            getNearbyFoodsData.getFlow(req).collect {
                _nearbyFoodResult.value = it
                if (it.isSuccess() && it.hasNextPage() && !it.data.isNullOrEmpty()) {
                    it.nextPageToken?.let { token ->
                        savedFoodResult.clear()
                        savedFoodResult.addAll(it.data)
                        saveToken = token
                        Handler().postDelayed({ getMorePageFoods() }, 2000)
                    }
                }
            }
        }
    }

    fun getMorePageFoods() {
        val req = PlaceReq.create(24.991488, 121.511536, pageToken = saveToken)
        viewModelScope.launch {
            getNearbyFoodsData.getFlow(req).collect {
                if (it.isFinished()) {
                    if (!it.data.isNullOrEmpty()) {
                        savedFoodResult.addAll(it.data)
                    }
                    if (it.hasNextPage()) {
                        it.nextPageToken?.let { token ->
                            saveToken = token
                            Handler().postDelayed({ getMorePageFoods() }, 2000)
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