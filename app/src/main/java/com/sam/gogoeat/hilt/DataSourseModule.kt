package com.sam.gogoeat.hilt

import com.sam.gogoeat.api.repository.datasourse.DataSource
import com.sam.gogoeat.api.repository.datasourse.LocalDataSource
import com.sam.gogoeat.api.repository.datasourse.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
class DataSourseModule {
    @LocalData
    @Provides
    fun providerLocalDataSource(): DataSource {
        return LocalDataSource()
    }

    @RemoteData
    @Provides
    fun providerRemoteDataSource(): DataSource {
        return RemoteDataSource()
    }
}