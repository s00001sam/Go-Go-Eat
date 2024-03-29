package com.sam.gogoeat.view.luckyresult

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sam.gogoeat.data.GogoPlace
import com.sam.gogoeat.data.place.PlaceData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(): ViewModel() {

    private val _newPlace = MutableStateFlow<GogoPlace>(GogoPlace())
    val newPlace : StateFlow<GogoPlace> = _newPlace

    private val _leaveControl = MutableStateFlow<Boolean>(false)
    val leaveControl : StateFlow<Boolean> = _leaveControl

    fun setNewPlace(gogoPlace: GogoPlace) {
        _newPlace.value = gogoPlace
    }

    fun leave() {
        _leaveControl.value = true
    }

    fun nothing() {}

    fun leaveComplete() {
        _leaveControl.value = false
    }

}