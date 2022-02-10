package com.sam.gogoeat.api.usecase

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.sam.gogoeat.api.repository.Repository
import com.sam.gogoeat.api.resp.base.Resource
import com.sam.gogoeat.api.usecase.basic.CallbackUseCase
import kotlinx.coroutines.flow.Flow

class GetMyLocation(private var repository: Repository) :
    CallbackUseCase<FusedLocationProviderClient, LatLng?, Resource<LatLng>>()
{
    override suspend fun getResponse(params: FusedLocationProviderClient): Flow<LatLng?> {
        return repository.getLocation(params)
    }

}