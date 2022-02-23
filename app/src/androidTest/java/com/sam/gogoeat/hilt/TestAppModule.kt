package com.sam.gogoeat.hilt

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.sam.gogoeat.room.RoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_roomDB")
    fun provideInMemoryDB(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, RoomDB::class.java).allowMainThreadQueries().build()
}