package com.sam.gogoeat.api.usecase.basic

import android.util.Log
import com.sam.gogoeat.api.resp.*
import com.sam.gogoeat.api.resp.base.ErrorUtil
import com.sam.gogoeat.api.resp.base.IResp
import com.sam.gogoeat.api.resp.base.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import retrofit2.Response

abstract class ResourceUseCase<out Type : Resource<ResponseValue>, in Params, ResponseValue : Any, ResponseType : IResp<ResponseValue>> : FlowUseCase<Type, Params>() {
    abstract suspend fun getResponse(params: Params): Response<ResponseType>
    open suspend fun processAfterGetResource(params: Params, resource: Resource<ResponseValue>): Resource<ResponseValue> {
        return resource
    }

    override fun getFlow(params: Params): Flow<Type> {
        return flow {
            var resource = getResource(params)
            resource = processAfterGetResource(params, resource)
            emit(resource as Type)
        }
                .flowOn(Dispatchers.IO)
                .onStart {
                    emit(Resource.loading(null) as Type)
                }
    }

    suspend fun getResource(params: Params): Resource<ResponseValue> {
        val finalResource: Resource<ResponseValue>
        var result: Response<ResponseType>? = null
        try {
            result = getResponse(params)
        } catch (e: Exception) {
            Log.i("sam",e.toString())
            return Resource.error(ErrorUtil.getErrorMsg(e))
        }

        if (result.isSuccessful) {
            val body = result.body()
            if (body != null) {
                finalResource = when (body) {
                    is MapResp<*> -> {
                        processMapRespResource(body as MapResp<ResponseValue>)
                    }
                    else -> {
                        Resource.error( "wrong format type")
                    }
                }
            } else {
                finalResource = Resource.error("empty body")
            }
        } else {
            finalResource = Resource.error(result.message())
        }
        return finalResource
    }

    private fun processMapRespResource(response: MapResp<ResponseValue>): Resource<ResponseValue> {
        if (response.isSuccess())
            return Resource.success(response.data, response.nextPageToken)
        else
            return Resource.error(response.error ?: "processMapRespResource, empty error")
    }
}