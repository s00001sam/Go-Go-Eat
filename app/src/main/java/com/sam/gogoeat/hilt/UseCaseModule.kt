package com.sam.gogoeat.hilt

import com.sam.gogoeat.api.repository.BaseRepository
import com.sam.gogoeat.api.usecase.GetHistories
import com.sam.gogoeat.api.usecase.GetMyLocation
import com.sam.gogoeat.api.usecase.GetNearbyFoodsData
import com.sam.gogoeat.api.usecase.InsertHistoryItem
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
class UseCaseModule {

    @Provides
    fun provideGetNearbyFoodsData(repository: BaseRepository) = GetNearbyFoodsData(repository)

    @Provides
    fun provideGetLocation(repository: BaseRepository) = GetMyLocation(repository)

    @Provides
    fun provideInsertHistoryItem(repository: BaseRepository) = InsertHistoryItem(repository)

    @Provides
    fun provideGetHistories(repository: BaseRepository) = GetHistories(repository)
}
