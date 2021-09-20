package com.sam.gogoeat.api.repository.datasourse

import com.sam.gogoeat.api.resp.MapResp
import com.sam.gogoeat.api.resp.NewsResp
import com.sam.gogoeat.data.Article
import com.sam.gogoeat.data.place.PlaceData
import retrofit2.Response
import retrofit2.http.Query

interface DataSource {

    suspend fun getTopHeadlines(country: String? = null,
                                category: String? = null,
                                q: String? = null,
                                pageSize: Int? = null,
                                page: Int? = null): Response<NewsResp<List<Article>>>

    suspend fun getNearbyPlaces(location: String? = null,
                                radius: Int? = null,
                                type: String? = null,
                                keyword: String? = null,
                                key: String? = null): Response<MapResp<List<PlaceData>>>

}