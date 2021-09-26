package com.sam.gogoeat.hilt

import com.sam.gogoeat.api.repository.Repository
import com.sam.gogoeat.api.usecase.GetNearbyFoodsData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
class UseCaseModule {

    @Provides
    fun provideGetNearbyFoodsData(repository: Repository) = GetNearbyFoodsData(repository)
}
