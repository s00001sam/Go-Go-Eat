package com.sam.gogoeat.api.usecase

import com.sam.gogoeat.api.repository.Repository
import com.sam.gogoeat.api.resp.base.State
import com.sam.gogoeat.api.usecase.basic.FlowUseCase
import com.sam.gogoeat.data.GogoPlace
import kotlinx.coroutines.flow.Flow

class InsertHistoryItem(private var repository: Repository) :
    FlowUseCase<GogoPlace, Long?, State<Long>>()
{
    override suspend fun getFlow(params: GogoPlace): Flow<Long?> {
        return repository.insertHistoryItem(gogoPlace = params)
    }

}