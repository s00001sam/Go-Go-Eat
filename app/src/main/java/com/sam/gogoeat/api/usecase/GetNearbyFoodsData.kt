package com.sam.gogoeat.api.usecase

import com.sam.gogoeat.api.repository.Repository
import com.sam.gogoeat.api.resp.MapResp
import com.sam.gogoeat.api.resp.base.Resource
import com.sam.gogoeat.api.usecase.basic.ResourceUseCase
import com.sam.gogoeat.data.place.PlaceData
import com.sam.gogoeat.data.place.PlaceReq
import retrofit2.Response

class GetNearbyFoodsData(private var repository: Repository) : ResourceUseCase<Resource<List<PlaceData>>, PlaceReq, List<PlaceData>, MapResp<List<PlaceData>>>() {

    override suspend fun getResponse(params: PlaceReq): Response<MapResp<List<PlaceData>>> {
        return repository.getNearbyPlaces(params.location, params.radius, params.type, params.keyword, params.key)
    }
}