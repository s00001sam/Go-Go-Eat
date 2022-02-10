package com.sam.gogoeat.api.usecase

import com.sam.gogoeat.api.repository.Repository
import com.sam.gogoeat.api.resp.MapResp
import com.sam.gogoeat.api.resp.base.Resource
import com.sam.gogoeat.api.usecase.basic.ResourceUseCase
import com.sam.gogoeat.data.place.PlaceData
import com.sam.gogoeat.data.place.PlaceReq
import com.sam.gogoeat.utils.Logger
import kotlinx.coroutines.delay
import retrofit2.Response

class GetNearbyFoodsData(private var repository: Repository) :
    ResourceUseCase<Resource<List<PlaceData>>, PlaceReq, List<PlaceData>, MapResp<List<PlaceData>>>()
{
    private var preToken: String? = null

    override suspend fun getResponse(params: PlaceReq): Response<MapResp<List<PlaceData>>> {
        return repository.getNearbyPlaces(
            params.location, params.radius, params.type, params.keyword, params.key,
            params.opennow, params.minprice, params.maxprice, params.pageToken)
    }

    override suspend fun processAfterGetResource(
        params: PlaceReq,
        resource: Resource<List<PlaceData>>
    ): Resource<List<PlaceData>> {
        if (!resource.hasNextPage() || resource.nextPageToken == preToken) return resource
        preToken = resource.nextPageToken
        params.pageToken = resource.nextPageToken
        delay(2000)
        val moreRes = getResource(params)
        val newRes = processAfterGetResource(params, resource.merge(moreRes))
        return newRes
    }

    private fun Resource<List<PlaceData>>.merge(moreRes: Resource<List<PlaceData>>): Resource<List<PlaceData>> {
        if (moreRes.data.isNullOrEmpty()) return this
        val newRes = mutableListOf<PlaceData>()
        this.data?.let { newRes.addAll(it) }
        moreRes.data.let { newRes.addAll(it) }
        return Resource.success(newRes, moreRes.nextPageToken)
    }
}