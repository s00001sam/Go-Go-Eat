package com.sam.gogoeat.api.repository

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.sam.gogoeat.api.repository.datasourse.DataSource
import com.sam.gogoeat.api.resp.MapResp
import com.sam.gogoeat.data.place.PlaceData
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class BaseRepository (
        private val remoteDataSource: DataSource,
        private val localDataSource: DataSource,
) : Repository {

    override suspend fun getNearbyPlaces(location: String?, radius: Int?, type: String?, keyword: String?, key: String?, opennow: Boolean?, pageToken: String?): Response<MapResp<List<PlaceData>>> {
        return remoteDataSource.getNearbyPlaces(location, radius, type, keyword, key, opennow, pageToken)
    }

    override suspend fun getLocation(client: FusedLocationProviderClient): Flow<LatLng?> {
        return localDataSource.getLocation(client)
    }


}