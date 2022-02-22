package com.sam.gogoeat.hilt

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sam.gogoeat.api.apiservice.PlaceApi
import com.sam.gogoeat.api.repository.BaseRepository
import com.sam.gogoeat.api.repository.datasourse.DataSource
import com.sam.gogoeat.room.RoomDB
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    private val API_URL = "https://maps.googleapis.com/maps/api/place/"

    @Singleton
    @Provides
    fun provideMoshi() = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi, client: OkHttpClient) = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(API_URL)
        .client(client)
        .build()

    @Singleton
    @Provides
    fun provideMapApi(retrofit: Retrofit) = retrofit.create(PlaceApi::class.java)

    @Singleton
    @Provides
    fun provideRoomDB(@ApplicationContext context: Context) = RoomDB.getInstance(context)

    @Singleton
    @Provides
    fun providePlaceDao(roomDB: RoomDB) = roomDB.placeDao()

    @Singleton
    @Provides
    fun provideRepository(@RemoteData remoteDataSource: DataSource, @LocalData localDataSource: DataSource) = BaseRepository(remoteDataSource, localDataSource)


}