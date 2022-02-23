package com.sam.gogoeat.repository

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.sam.gogoeat.api.repository.Repository
import com.sam.gogoeat.api.resp.MapResp
import com.sam.gogoeat.data.GogoPlace
import com.sam.gogoeat.data.place.PlaceData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakeRepository: Repository {

    val historyPlaces = mutableListOf<GogoPlace>()

    private var shouldReturnNetError : Boolean = false

    fun setShouldReturnNetError(isError: Boolean) {
        shouldReturnNetError = isError
    }

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
    ): Response<MapResp<List<PlaceData>>> {
        return if (!shouldReturnNetError) {
            delay(200)
            Response.success(MapResp("OK", listOf(), null, null))
        } else {
            delay(200)
            Response.error(400, "Net Error".toResponseBody())
        }
    }

    override suspend fun getLocation(client: FusedLocationProviderClient): Flow<LatLng?> = flow {
        delay(200)
        emit(LatLng(122.1, 23.5))
    }

    override suspend fun insertHistoryItem(gogoPlace: GogoPlace): Flow<Long?> = flow {
        historyPlaces.add(gogoPlace)
        delay(200)
        emit(1L)
    }

    override suspend fun getAllHistoryItem(): Flow<List<GogoPlace>?> = flow {
        delay(200)
        emit(historyPlaces)
    }
}