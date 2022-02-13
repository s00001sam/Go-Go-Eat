package com.sam.gogoeat.hilt

import com.sam.gogoeat.api.apiservice.PlaceApi
import com.sam.gogoeat.api.repository.datasourse.DataSource
import com.sam.gogoeat.api.repository.datasourse.LocalDataSource
import com.sam.gogoeat.api.repository.datasourse.RemoteDataSource
import com.sam.gogoeat.room.RoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataSourseModule {
    @Singleton
    @LocalData
    @Provides
    fun providerLocalDataSource(roomDB: RoomDB): DataSource {
        return LocalDataSource(roomDB)
    }

    @Singleton
    @RemoteData
    @Provides
    fun providerRemoteDataSource(api: PlaceApi): DataSource {
        return RemoteDataSource(api)
    }
}