package com.sam.gogoeat.view.lotteryhistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.gogoeat.api.resp.base.State
import com.sam.gogoeat.api.usecase.GetHistories
import com.sam.gogoeat.data.GogoPlace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LotteryHistoryViewModel @Inject constructor(
    private val getHistories: GetHistories
) : ViewModel() {

    private val _getHistoriesResult = MutableStateFlow<State<List<GogoPlace>?>>(State.nothing())
    val getHistoriesResult : StateFlow<State<List<GogoPlace>?>> = _getHistoriesResult

    init {
        getHistories()
    }

    fun getHistories() {
        viewModelScope.launch {
            getHistories.getDataState(null).collect {
                _getHistoriesResult.value = it
            }
        }
    }
}