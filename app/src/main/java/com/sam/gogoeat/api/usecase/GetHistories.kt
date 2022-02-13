package com.sam.gogoeat.api.usecase

import com.sam.gogoeat.api.repository.Repository
import com.sam.gogoeat.api.resp.base.State
import com.sam.gogoeat.api.usecase.basic.FlowUseCase
import com.sam.gogoeat.data.GogoPlace
import kotlinx.coroutines.flow.Flow

class GetHistories(private var repository: Repository) :
    FlowUseCase<Any?, List<GogoPlace>?, State<List<GogoPlace>?>>()
{
    override suspend fun getFlow(params: Any?): Flow<List<GogoPlace>?> {
        return repository.getAllHistoryItem()
    }

}