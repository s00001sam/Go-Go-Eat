package com.sam.gogoeat.api.usecase

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.sam.gogoeat.api.repository.Repository
import com.sam.gogoeat.api.resp.base.State
import com.sam.gogoeat.api.usecase.basic.FlowUseCase
import kotlinx.coroutines.flow.Flow

class GetMyLocation(private var repository: Repository) :
    FlowUseCase<FusedLocationProviderClient, LatLng?, State<LatLng>>()
{
    override suspend fun getFlow(params: FusedLocationProviderClient): Flow<LatLng?> {
        return repository.getLocation(params)
    }

}