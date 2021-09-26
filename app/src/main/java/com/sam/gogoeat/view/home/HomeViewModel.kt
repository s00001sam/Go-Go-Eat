package com.sam.gogoeat.view.home

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
class HomeViewModel @Inject constructor(private val getNearbyFoodsData: GetNearbyFoodsData) : ViewModel() {

    private val _nearbyFoodResult = MutableStateFlow<Resource<List<PlaceData>>>(Resource.nothing(null))
    val nearbyFoodResult : StateFlow<Resource<List<PlaceData>>> = _nearbyFoodResult

    fun getNearbyFoods() {
        val req = PlaceReq.create(24.991488, 121.511536)
        viewModelScope.launch {
            getNearbyFoodsData.getFlow(req).collect {
                _nearbyFoodResult.value = it
            }
        }

    }

}