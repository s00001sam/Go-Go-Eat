package com.sam.gogoeat.api.repository

import com.sam.gogoeat.api.repository.datasourse.DataSource
import com.sam.gogoeat.api.resp.MapResp
import com.sam.gogoeat.data.Article
import com.sam.gogoeat.data.place.PlaceData
import retrofit2.Response

class Repository (
        private val remoteDataSource: DataSource,
        private val localDataSource: DataSource,
) {

    suspend fun getNearbyPlaces(location: String? = null, radius: Int? = null, type: String? = null, keyword: String? = null, key: String? = null, opennow: Boolean?, pageToken: String?): Response<MapResp<List<PlaceData>>> {
        return remoteDataSource.getNearbyPlaces(location, radius, type, keyword, key, opennow, pageToken)
    }


}