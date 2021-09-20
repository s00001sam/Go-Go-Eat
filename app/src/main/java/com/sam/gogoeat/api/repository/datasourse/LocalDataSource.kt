package com.sam.gogoeat.api.repository.datasourse

import com.sam.gogoeat.api.resp.MapResp
import com.sam.gogoeat.api.resp.NewsResp
import com.sam.gogoeat.data.Article
import com.sam.gogoeat.data.place.PlaceData
import retrofit2.Response

class LocalDataSource : DataSource {
    override suspend fun getTopHeadlines(country: String?, category: String?, q: String?, pageSize: Int?, page: Int?): Response<NewsResp<List<Article>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getNearbyPlaces(location: String?, radius: Int?, type: String?, keyword: String?, key: String?): Response<MapResp<List<PlaceData>>> {
        TODO("Not yet implemented")
    }
}