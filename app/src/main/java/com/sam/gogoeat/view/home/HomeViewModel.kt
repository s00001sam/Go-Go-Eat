package com.sam.gogoeat.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.gogoeat.api.resp.base.Resource
import com.sam.gogoeat.api.usecase.GetTopHeadlinesData
import com.sam.gogoeat.data.Article
import com.sam.gogoeat.data.HeadlineReq
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getTopHeadlinesData: GetTopHeadlinesData) : ViewModel() {

//    private val _articles = MutableStateFlow<Resource<List<Article>>>(Resource.nothing(null))
//    val articles : StateFlow<Resource<List<Article>>> = _articles

//    fun getHeadlineNews() {
//        val req = HeadlineReq(country = "tw")
//        viewModelScope.launch {
//            getTopHeadlinesData.getFlow(req).collect {
//                _articles.value = it
//            }
//        }
//
//    }



}