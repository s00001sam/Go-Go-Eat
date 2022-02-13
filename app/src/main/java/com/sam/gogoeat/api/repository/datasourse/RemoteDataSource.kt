package com.sam.gogoeat.api.repository.datasourse

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.sam.gogoeat.api.apiservice.PlaceApi
import com.sam.gogoeat.data.GogoPlace
import kotlinx.coroutines.flow.Flow

class RemoteDataSource(private val api: PlaceApi) : DataSource {

    override suspend fun getNearbyPlaces(
        location: String?,
        radius: Int?,
        type: String?,
        keyword: String?,
        key: String?,
        opennow: Boolean?,
        minprice: Int?,
        maxprice: Int?,
        pageToken: String?
    ) = api.getNearbyPlaces(location, radius, type, keyword, key, opennow, minprice, maxprice, pageToken)

    override suspend fun getLocation(client: FusedLocationProviderClient): Flow<LatLng?> {
        TODO("Not yet implemented")
    }

    override suspend fun insertHistoryItem(gogoPlace: GogoPlace): Flow<Long?> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllHistoryItem(): Flow<List<GogoPlace>?> {
        TODO("Not yet implemented")
    }

}