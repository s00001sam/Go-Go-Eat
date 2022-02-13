package com.sam.gogoeat.api.usecase.basic

import com.sam.gogoeat.api.resp.base.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

abstract class FlowUseCase<Params, Type, Res : State<Type>> {
    abstract suspend fun getFlow(params: Params): Flow<Type>

    open suspend fun getDataState(params: Params): Flow<Res> {
        return getFlow(params)
            .transform {
                if (it == null) {
                    emit(State.error<Type>( "get location error") as Res)
                } else {
                    emit(State.success(it) as Res)
                }
            }
                .flowOn(Dispatchers.IO)
                .onStart {
                    emit(State.loading(null) as Res)
                }
    }
}