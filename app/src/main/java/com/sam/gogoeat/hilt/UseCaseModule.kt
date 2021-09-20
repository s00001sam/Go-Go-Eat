package com.sam.gogoeat.hilt

import com.sam.gogoeat.api.repository.Repository
import com.sam.gogoeat.api.usecase.GetNearbyFoodsData
import com.sam.gogoeat.api.usecase.GetTopHeadlinesData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
class UseCaseModule {
    @Provides
    fun provideGetTopHeadlinesData(repository: Repository) = GetTopHeadlinesData(repository)

    @Provides
    fun provideGetNearbyFoodsData(repository: Repository) = GetNearbyFoodsData(repository)
}
