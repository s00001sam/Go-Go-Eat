package com.sam.gogoeat.api.apiservice

import com.sam.gogoeat.api.resp.MapResp
import com.sam.gogoeat.api.resp.NewsResp
import com.sam.gogoeat.data.Article
import com.sam.gogoeat.data.place.PlaceData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LifeApi {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
            @Query("country") country: String? = null,
            @Query("category") category: String? = null,
            @Query("q") q: String? = null,
            @Query("pageSize") pageSize: Int? = null,
            @Query("page") page: Int? = null
    ): Response<NewsResp<List<Article>>>

    @GET("nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location") location: String? = null,
        @Query("radius") radius: Int? = null,
        @Query("type") type: String? = null,
        @Query("keyword") keyword: String? = null,
        @Query("key") key: String? = null
    ): Response<MapResp<List<PlaceData>>>

}