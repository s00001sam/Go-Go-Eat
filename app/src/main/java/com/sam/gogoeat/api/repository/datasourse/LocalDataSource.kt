package com.sam.gogoeat.api.repository.datasourse

import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.sam.gogoeat.api.resp.MapResp
import com.sam.gogoeat.data.GogoPlace
import com.sam.gogoeat.data.place.PlaceData
import com.sam.gogoeat.room.RoomDB
import com.sam.gogoeat.utils.Util
import com.sam.gogoeat.view.MainActivity
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class LocalDataSource(private val roomDB: RoomDB) : DataSource {

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
        TODO("Not yet implemented")
    }


    override suspend fun getLocation(
        client: FusedLocationProviderClient
    ): Flow<LatLng?> = callbackFlow {
        if (!Util.checkHasPermission(MainActivity.LOCATION_FINE)
            && !Util.checkHasPermission(MainActivity.LOCATION_COARSE)
        ) {
            trySend(null)
        }
        val callBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val lastLocation = locationResult.lastLocation
                val latlng = LatLng(
                    lastLocation?.latitude ?: 121.0,
                    lastLocation?.longitude ?: 25.0,
                )
                trySend(latlng)
            }
        }

        val locationReq = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 10000
        }

        client.requestLocationUpdates(locationReq, callBack, Looper.getMainLooper())
        awaitClose { client.removeLocationUpdates(callBack) }
    }

    override suspend fun insertHistoryItem(gogoPlace: GogoPlace) = flow {
        val complete = roomDB.placeDao().insertOne(gogoPlace)
        emit(complete)
    }

    override suspend fun getAllHistoryItem() = flow {
        val list = roomDB.placeDao().getAll()
        emit(list)
    }
}
