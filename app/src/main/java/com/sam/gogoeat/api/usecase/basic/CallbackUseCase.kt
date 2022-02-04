package com.sam.gogoeat.api.usecase.basic

import com.sam.gogoeat.api.resp.base.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

abstract class CallbackUseCase<Params, Type, Res : Resource<Type>> {
    abstract suspend fun getResponse(params: Params): Flow<Type>

    open suspend fun getFlow(params: Params): Flow<Res> {
        return getResponse(params)
            .transform {
                if (it == null) {
                    emit(Resource.error<Type>( "get location error") as Res)
                } else {
                    emit(Resource.success(it) as Res)
                }
            }
                .flowOn(Dispatchers.IO)
                .onStart {
                    emit(Resource.loading(null) as Res)
                }
    }
}