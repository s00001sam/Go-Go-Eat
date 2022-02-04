package com.sam.gogoeat.api.repository.datasourse

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.sam.gogoeat.api.apiservice.MapApi
import com.sam.gogoeat.api.resp.MapResp
import com.sam.gogoeat.data.place.PlaceData
import kotlinx.coroutines.flow.Flow
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

    override suspend fun getLocation(client: FusedLocationProviderClient): Flow<LatLng?> {
        TODO("Not yet implemented")
    }

}