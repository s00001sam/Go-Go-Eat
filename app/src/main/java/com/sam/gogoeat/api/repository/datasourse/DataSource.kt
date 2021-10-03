package com.sam.gogoeat.api.repository.datasourse

import com.sam.gogoeat.api.resp.MapResp
import com.sam.gogoeat.data.Article
import com.sam.gogoeat.data.place.PlaceData
import retrofit2.Response

interface DataSource {

    suspend fun getNearbyPlaces(location: String? = null,
                                radius: Int? = null,
                                type: String? = null,
                                keyword: String? = null,
                                key: String? = null,
                                opennow: Boolean? = null,
                                pageToken: String? = null): Response<MapResp<List<PlaceData>>>

}