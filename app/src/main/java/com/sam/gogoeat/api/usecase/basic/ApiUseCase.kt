package com.sam.gogoeat.api.usecase.basic

import android.util.Log
import com.sam.gogoeat.api.resp.*
import com.sam.gogoeat.utils.ErrorUtil
import com.sam.gogoeat.api.resp.base.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import retrofit2.Response

abstract class ApiUseCase<out Type : State<ResData>, in Req, ResData : Any, ResponseType : Any> {
    abstract suspend fun getApiData(req: Req): Response<ResponseType>
    fun getDataState(req: Req): Flow<Type> {
        return flow {
            var resource = getResource(req)
            resource = processAfterGetResource(req, resource)
            emit(resource as Type)
        }
                .flowOn(Dispatchers.IO)
                .onStart {
                    emit(State.loading(null) as Type)
                }
    }

    suspend fun getResource(params: Req): State<ResData> {
        val finalState: State<ResData>
        var result: Response<ResponseType>? = null
        try {
            result = getApiData(params)
        } catch (e: Exception) {
            Log.i("sam",e.toString())
            return State.error(ErrorUtil.getErrorMsg(e))
        }

        if (result.isSuccessful) {
            val body = result.body()
            if (body != null) {
                finalState = when (body) {
                    is MapResp<*> -> {
                        handleMapResp(body as MapResp<ResData>)
                    }
                    else -> {
                        State.error( "no define response format")
                    }
                }
            } else {
                finalState = State.error("empty body")
            }
        } else {
            finalState = State.error(result.message())
        }
        return finalState
    }

    private fun handleMapResp(response: MapResp<ResData>): State<ResData> {
        if (response.isSuccess())
            return State.success(response.data, response.nextPageToken)
        else
            return State.error(response.error ?: "processMapRespResource, empty error")
    }

    open suspend fun processAfterGetResource(req: Req, state: State<ResData>): State<ResData> {
        return state
    }

}