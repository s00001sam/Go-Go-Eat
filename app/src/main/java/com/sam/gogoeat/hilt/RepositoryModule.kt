package com.sam.gogoeat.hilt

import com.sam.gogoeat.api.repository.Repository
import com.sam.gogoeat.api.repository.datasourse.DataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
class RepositoryModule {
    @Provides
    fun provideRepository(@RemoteData remoteDataSource: DataSource, @LocalData localDataSource: DataSource,) = Repository(remoteDataSource, localDataSource)
}