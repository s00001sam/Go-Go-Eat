package com.sam.gogoeat.api.repository

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.sam.gogoeat.api.resp.MapResp
import com.sam.gogoeat.data.place.PlaceData
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface Repository {

    suspend fun getNearbyPlaces(
        location: String? = null, radius: Int? = null, type: String? = null, keyword: String? = null,
        key: String? = null, opennow: Boolean?, pageToken: String?
    ): Response<MapResp<List<PlaceData>>>

    suspend fun getLocation(client: FusedLocationProviderClient) : Flow<LatLng?>
}