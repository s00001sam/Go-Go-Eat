package com.sam.gogoeat.api.usecase.basic

import android.util.Log
import com.sam.gogoeat.api.resp.*
import com.sam.gogoeat.api.resp.base.IResp
import com.sam.gogoeat.api.resp.base.Resource
import com.sam.gogoeat.api.resp.base.StatusCode
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
            Log.i("sam00","flow currentThread: ${Thread.currentThread().name}")
            var resource = getResource(params)
            resource = processAfterGetResource(params, resource)
            emit(resource as Type)
        }
                .flowOn(Dispatchers.IO)
                .onStart {
                    Log.i("sam00","onStart currentThread: ${Thread.currentThread().name}")
                    emit(Resource.loading(null) as Type)
                }
    }

    suspend fun getResource(params: Params): Resource<ResponseValue> {
        val finalResource: Resource<ResponseValue>
        var result: Response<ResponseType>? = null
        try {
            result = getResponse(params)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("sam",e.toString())
            if (e is HttpException) {
                return Resource.error(e.code(), e.message())
            } else {
                return Resource.error(StatusCode.CODE_ERROR_JSON_ERROR, e.toString())
            }
        }

        if (result.isSuccessful) {
            val body = result.body()
            if (body != null) {
                finalResource = when (body) {
                    is BaseResp<*> -> {
                        processBaseRespResource(body as BaseResp<ResponseValue>)
                    }
                    is NewsResp<*> -> {
                        processNewsRespResource(body as NewsResp<ResponseValue>)
                    }
                    is MapResp<*> -> {
                        processMapRespResource(body as MapResp<ResponseValue>)
                    }
                    else -> {
                        Resource.error(StatusCode.CODE_ERROR_JSON_ERROR, "wrong format type")
                    }
                }
            } else {
                finalResource = Resource.error(result.code(), "empty body")
            }
        } else {
            finalResource = Resource.error(result.code(), result.message())
        }
        return finalResource
    }

    private fun processBaseRespResource(response: BaseResp<ResponseValue>): Resource<ResponseValue> {
        return if (response.isSuccess())
            Resource.success(response.data)
        else
            Resource.error(response.status?.code
                    ?: StatusCode.CODE_ERROR_JSON_ERROR, response.status?.msg
                    ?: "processBaseRespResource, empty error")
    }

    private fun processNewsRespResource(response: NewsResp<ResponseValue>): Resource<ResponseValue> {
        return if (response.isSuccess())
            Resource.success(response.data)
        else
            Resource.error(StatusCode.CODE_ERROR_JSON_ERROR, response.message
                    ?: "processNewsRespResource, empty error")
    }

    private fun processMapRespResource(response: MapResp<ResponseValue>): Resource<ResponseValue> {
        return if (response.isSuccess())
            Resource.success(response.data)
        else
            Resource.error(StatusCode.CODE_ERROR_JSON_ERROR, response.error_message
                ?: "processMapRespResource, empty error")
    }
}