package com.sam.gogoeat.api.repository.datasourse

import com.sam.gogoeat.api.apiservice.MapApi
import com.sam.gogoeat.api.resp.MapResp
import com.sam.gogoeat.data.place.PlaceData
import retrofit2.Response

class RemoteDataSource : DataSource {

    override suspend fun getNearbyPlaces(
        location: String?,
        radius: Int?,
        type: String?,
        keyword: String?,
        key: String?,
        opennow: Boolean?,
        pageToken: String?
    ) = MapApi.apiService.getNearbyPlaces(location, radius, type, keyword, key, opennow, pageToken)


}