package com.sam.gogoeat.view.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _isListIconClick = MutableStateFlow<Boolean>(false)
    val isListIconClick: StateFlow<Boolean> = _isListIconClick

    fun setIsListOpen(isOpen: Boolean) {
        _isListIconClick.value = isOpen
    }
}