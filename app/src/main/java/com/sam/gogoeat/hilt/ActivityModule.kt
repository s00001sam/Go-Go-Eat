package com.sam.gogoeat.hilt

import com.sam.gogoeat.api.repository.BaseRepository
import com.sam.gogoeat.api.repository.datasourse.DataSource
import com.sam.gogoeat.view.support.PressBackHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
class ActivityModule {
    @Provides
    fun provideRepository(@RemoteData remoteDataSource: DataSource, @LocalData localDataSource: DataSource) = BaseRepository(remoteDataSource, localDataSource)

    @Provides
    fun provideBackHelper() = PressBackHelper()
}