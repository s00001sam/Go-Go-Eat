package com.sam.gogoeat.api.usecase

import com.sam.gogoeat.api.repository.Repository
import com.sam.gogoeat.api.resp.MapResp
import com.sam.gogoeat.api.resp.base.State
import com.sam.gogoeat.api.usecase.basic.ApiUseCase
import com.sam.gogoeat.data.place.PlaceData
import com.sam.gogoeat.data.place.PlaceReq
import kotlinx.coroutines.delay
import retrofit2.Response

class GetNearbyFoodsData(private var repository: Repository) :
    ApiUseCase<State<List<PlaceData>>, PlaceReq, List<PlaceData>, MapResp<List<PlaceData>>>()
{
    private var preToken: String? = null

    override suspend fun getApiData(req: PlaceReq): Response<MapResp<List<PlaceData>>> {
        return repository.getNearbyPlaces(
            req.location, req.radius, req.type, req.keyword, req.key,
            req.opennow, req.minprice, req.maxprice, req.pageToken)
    }

    override suspend fun processAfterGetResource(
        params: PlaceReq,
        state: State<List<PlaceData>>
    ): State<List<PlaceData>> {
        if (!state.hasNextPage() || state.nextPageToken == preToken) return state
        preToken = state.nextPageToken
        params.pageToken = state.nextPageToken
        delay(2000)
        val moreRes = getResource(params)
        val newRes = processAfterGetResource(params, state.merge(moreRes))
        return newRes
    }

    private fun State<List<PlaceData>>.merge(moreRes: State<List<PlaceData>>): State<List<PlaceData>> {
        if (moreRes.data.isNullOrEmpty()) return this
        val newRes = mutableListOf<PlaceData>()
        this.data?.let { newRes.addAll(it) }
        moreRes.data.let { newRes.addAll(it) }
        return State.success(newRes, moreRes.nextPageToken)
    }
}